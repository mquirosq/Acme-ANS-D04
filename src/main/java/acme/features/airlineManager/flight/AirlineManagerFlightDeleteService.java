
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.MoneyExchangeHelper;
import acme.entities.ActivityLog;
import acme.entities.Flight;
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
import acme.forms.MoneyExchange;
import acme.helpers.FlightHelper;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean authorised;
		try {
			String flightIdInput = super.getRequest().getData("id", String.class);
			int flightId = Integer.parseInt(flightIdInput);
			Flight flight = this.repository.findFlightById(flightId);
			authorised = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager());
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
	}

	@Override
	public void validate(final Flight flight) {
		Collection<FlightLeg> legs;
		boolean allLegsUnpublished = true;

		legs = this.repository.findAllLegsByFlightId(flight.getId());

		for (FlightLeg l : legs)
			if (!l.getDraftMode())
				allLegsUnpublished = false;

		super.state(allLegsUnpublished, "*", "acme.validation.flight.publishedLegs.message");
	}

	@Override
	public void perform(final Flight flight) {
		Collection<FlightLeg> legs;
		Collection<FlightAssignment> assignments;
		Collection<ActivityLog> logs;

		legs = this.repository.findAllLegsByFlightId(flight.getId());
		assignments = this.repository.findAllAssignmentsByFlightId(flight.getId());
		logs = this.repository.findAllActivityLogsByFlightId(flight.getId());

		this.repository.deleteAll(logs);
		this.repository.deleteAll(assignments);
		this.repository.deleteAll(legs);
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		dataset = FlightHelper.unbindFlightDerivatedProperties(dataset, flight);

		String systemCurrency = this.repository.getSystemCurrency();

		Money exchangedCost;
		if (systemCurrency.equals(flight.getCost().getCurrency()))
			exchangedCost = null;
		else {
			MoneyExchange exchange = new MoneyExchange();
			exchange.setSource(flight.getCost());
			exchange.setTargetCurrency(systemCurrency);
			exchange = MoneyExchangeHelper.performExchangeToSystemCurrency(exchange);
			exchangedCost = exchange.getTarget();
			super.state(exchange.getOops() == null, "*", exchange.getMessage());
		}
		dataset.put("systemCost", exchangedCost);
		super.getResponse().addData(dataset);
	}
}
