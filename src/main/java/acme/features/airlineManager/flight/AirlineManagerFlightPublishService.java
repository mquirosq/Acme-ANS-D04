
package acme.features.airlineManager.flight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.MoneyExchangeHelper;
import acme.entities.Flight;
import acme.forms.MoneyExchange;
import acme.helpers.FlightHelper;
import acme.helpers.ValidatorHelper;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

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
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		List<Boolean> validation = ValidatorHelper.validatePublishedFlight(flight);

		boolean atLeastOneLeg = validation.get(0);
		boolean isNotOverlapping = validation.get(1);
		boolean allLegsPublished = validation.get(2);

		super.state(atLeastOneLeg, "*", "acme.validation.flight.missingLegs.message");
		super.state(isNotOverlapping, "*", "acme.validation.flight.overlappingDates.message");
		super.state(allLegsPublished, "*", "acme.validation.flight.notPublishedLeg.message");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		dataset = FlightHelper.unbindFlightDerivatedProperties(dataset, flight);
		String systemCurrency = this.repository.getSystemCurrency();

		Money exchangedCost = null;
		if (flight.getCost() != null && !systemCurrency.equals(flight.getCost().getCurrency())) {
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
