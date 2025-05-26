
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AircraftStatus;
import acme.entities.Aircraft;
import acme.entities.Airline;

@GuiService
public class AdministratorAircraftShowService extends AbstractGuiService<Administrator, Aircraft> {

	private final AdministratorAircraftRepository repository;


	@Autowired
	public AdministratorAircraftShowService(final AdministratorAircraftRepository repository) {
		this.repository = repository;
	}

	@Override
	public void authorise() {
		Boolean authorised;
		String rawId;
		int aircraftId;
		Aircraft aircraft;

		try {
			rawId = super.getRequest().getData("id", String.class);
			aircraftId = Integer.parseInt(rawId);
			aircraft = this.repository.findAircraftById(aircraftId);
			authorised = aircraft != null;
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id;
		Aircraft aircraft;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		Collection<Airline> airlines;
		SelectChoices statusChoices;
		SelectChoices airlineChoices;

		airlines = this.repository.findAllAirlines();

		statusChoices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());
		airlineChoices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "details");
		dataset.put("confirmation", false);
		dataset.put("readonly", true);
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
