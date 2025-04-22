
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AirportScope;
import acme.entities.Airport;

@GuiService
public class AdministratorAirportShowService extends AbstractGuiService<Administrator, Airport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Boolean authorised;
		String rawId;
		int airportId;
		Airport airport;

		try {
			rawId = super.getRequest().getData("id", String.class);
			airportId = Integer.parseInt(rawId);
			airport = this.repository.findAirportById(airportId);
			authorised = airport != null;
		} catch (NumberFormatException e) {
			authorised = false;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Airport airport;
		int id;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(AirportScope.class, airport.getScope());

		dataset = super.unbindObject(airport, "name", "IATACode", "scope", "website", "email", "phoneNumber", "city", "country");
		dataset.put("readonly", true);
		dataset.put("scopes", choices);

		super.getResponse().addData(dataset);
	}

}
