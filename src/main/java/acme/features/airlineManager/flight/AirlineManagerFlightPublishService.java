
package acme.features.airlineManager.flight;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight;
import acme.entities.FlightLeg;
import acme.entities.FlightLegRepository;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository	repository;

	@Autowired
	private FlightLegRepository				flightLegRepository;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);

		AirlineManager manager = flight.getManager();

		boolean authorised = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

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
		;
	}

	@Override
	public void validate(final Flight flight) {
		boolean isNotOverlapping = true;
		boolean allLegsPublished = true;
		boolean atLeastOneLeg = true;

		List<FlightLeg> legsOfFlight = this.flightLegRepository.getLegsOfFlight(flight.getId());

		if (legsOfFlight.size() <= 0)
			atLeastOneLeg = false;

		super.state(atLeastOneLeg, "*", "acme.validation.flight.missingLegs.message");

		for (Integer i = 0; i < legsOfFlight.size() - 1; i++) {
			{
				Date scheduledArrivalFirst = legsOfFlight.get(i).getScheduledArrival();
				Date scheduledDepartureSecond = legsOfFlight.get(i + 1).getScheduledDeparture();
				if (MomentHelper.isAfterOrEqual(scheduledArrivalFirst, scheduledDepartureSecond))
					isNotOverlapping = false;
			}
			{
				if (legsOfFlight.get(i).getDraftMode())
					allLegsPublished = false;
			}
		}

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
