
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int airlineManagerId;
		Collection<Flight> flights;

		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findAllFlightsByAirlineManagerId(airlineManagerId);
		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		String identifierCode = flight.getIdentifierCode();
		Integer numberOfLayovers = flight.getNumberOfLayovers();

		dataset = super.unbindObject(flight, "tag", "draftMode");

		dataset.put("identifierCode", identifierCode);
		dataset.put("numberOfLayovers", numberOfLayovers);

		super.addPayload(dataset, flight, "requiresSelfTransfer", "cost", "description");

		super.getResponse().addData(dataset);
	}

}
