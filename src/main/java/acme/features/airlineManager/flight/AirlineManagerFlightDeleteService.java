
package acme.features.airlineManager.flight;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.ActivityLog;
import acme.entities.Flight;
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
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

			AirlineManager manager = flight.getManager();

			authorised = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
		} catch (NumberFormatException e) {
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
		Dataset dataset;
		Date scheduledDeparture = flight.getScheduledDeparture();
		Date scheduledArrival = flight.getScheduledArrival();
		String originCity = flight.getOriginCity();
		String destinationCity = flight.getDestinationCity();
		Integer numberOfLayovers = flight.getNumberOfLayovers();

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");

		dataset.put("scheduledDeparture", scheduledDeparture != null ? scheduledDeparture : "-");
		dataset.put("scheduledArrival", scheduledArrival != null ? scheduledArrival : "-");
		dataset.put("originCity", originCity != null ? originCity : "-");
		dataset.put("destinationCity", destinationCity != null ? destinationCity : "-");
		dataset.put("numberOfLayovers", numberOfLayovers);

		super.getResponse().addData(dataset);
	}
}
