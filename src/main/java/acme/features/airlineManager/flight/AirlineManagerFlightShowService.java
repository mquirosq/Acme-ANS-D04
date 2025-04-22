
package acme.features.airlineManager.flight;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightShowService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean authorised = true;

		try {
			String flightIdInput = super.getRequest().getData("id", String.class);
			Integer.parseInt(flightIdInput);
		} catch (NumberFormatException e) {
			authorised = false;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
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
