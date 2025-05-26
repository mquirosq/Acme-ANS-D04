
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airline;

@GuiService
public class AdministratorAirlineListService extends AbstractGuiService<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	private final AdministratorAirlineRepository repository;


	@Autowired
	public AdministratorAirlineListService(final AdministratorAirlineRepository repository) {
		this.repository = repository;
	}

	// AbstractGuiService interface -------------------------------------------

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Airline> airlines;

		airlines = this.repository.findAllAirlines();

		super.getBuffer().addData(airlines);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;

		dataset = super.unbindObject(airline, "name", "IATACode", "type");
		super.addPayload(dataset, airline, "website", "foundation", "email", "phoneNumber");

		super.getResponse().addData(dataset);
	}
}
