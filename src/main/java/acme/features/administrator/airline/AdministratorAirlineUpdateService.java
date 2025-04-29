
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AirlineType;
import acme.entities.Airline;

@GuiService
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractService<Administrator, Airline> -------------------------------------


	@Override
	public void authorise() {
		Boolean authorised;
		String rawId;
		int airlineId;
		Airline airline;

		try {
			rawId = super.getRequest().getData("id", String.class);
			airlineId = Integer.parseInt(rawId);
			airline = this.repository.findAirlineById(airlineId);
			authorised = airline != null;
		} catch (NumberFormatException e) {
			authorised = false;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Airline airline;
		int id;

		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "IATACode", "type", "website", "email", "phoneNumber", "foundation");
	}

	@Override
	public void validate(final Airline airline) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(AirlineType.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "IATACode", "type", "website", "email", "phoneNumber", "foundation");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}
}
