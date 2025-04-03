
package acme.features.airlineManager.flight;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
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
		super.getResponse().setAuthorised(true);
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
		Collection<AirlineManager> managers;
		SelectChoices managerChoices;
		Dataset dataset;
		Date scheduledDeparture = flight.getScheduledDeparture();
		Date scheduledArrival = flight.getScheduledArrival();
		String originCity = flight.getOriginCity();
		String destinationCity = flight.getDestinationCity();
		Integer numberOfLayovers = flight.getNumberOfLayovers();

		managers = this.repository.findAllAirlineManagers();

		managerChoices = SelectChoices.from(managers, "identifierNumber", flight.getManager());

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");

		dataset.put("manager", managerChoices.getSelected().getKey());
		dataset.put("managers", managerChoices);
		dataset.put("scheduledDeparture", scheduledDeparture != null ? scheduledDeparture : "-");
		dataset.put("scheduledArrival", scheduledArrival != null ? scheduledArrival : "-");
		dataset.put("originCity", originCity != null ? originCity : "-");
		dataset.put("destinationCity", destinationCity != null ? destinationCity : "-");
		dataset.put("numberOfLayovers", numberOfLayovers);

		super.getResponse().addData(dataset);
	}

}
