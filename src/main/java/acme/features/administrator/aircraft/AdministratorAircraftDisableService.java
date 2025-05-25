
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
public class AdministratorAircraftDisableService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int aircraftId, airlineId;
		String aircraftIdRaw, airlineIdRaw;
		Aircraft aircraft;
		Airline airline;

		authorised = true;

		if (super.getRequest().hasData("id")) {
			aircraftIdRaw = super.getRequest().getData("id", String.class);

			try {
				aircraftId = Integer.parseInt(aircraftIdRaw);
			} catch (NumberFormatException | AssertionError e) {
				aircraftId = -1;
				authorised = false;
			}
			aircraft = this.repository.findAircraftById(aircraftId);
			authorised &= aircraft != null && !aircraft.getStatus().equals(AircraftStatus.UNDER_MAINTENANCE);
		} else
			authorised = false;

		if (super.getRequest().hasData("airline")) {
			airlineIdRaw = super.getRequest().getData("airline", String.class);

			try {
				airlineId = Integer.parseInt(airlineIdRaw);
			} catch (NumberFormatException e) {
				airlineId = -1;
				authorised = false;
			}

			if (airlineId != 0) {
				airline = this.repository.findAirlineById(airlineId);
				authorised &= airline != null;
			}
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
	public void bind(final Aircraft aircraft) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "details");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);

		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		aircraft.setStatus(AircraftStatus.UNDER_MAINTENANCE);
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		Collection<Airline> airlines;
		SelectChoices statusChoices, airlineChoices;

		airlines = this.repository.findAllAirlines();

		statusChoices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());
		airlineChoices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "details");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("statuses", statusChoices);
		dataset.put("status", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
