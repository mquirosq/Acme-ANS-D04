
package acme.features.airlineManager.flightLeg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.FlightLeg;
import acme.helpers.FlightHelper;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegShowService extends AbstractGuiService<AirlineManager, FlightLeg> {

	@Autowired
	private AirlineManagerFlightLegRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		String legIdInput;
		int legId, managerId;
		FlightLeg leg;
		AirlineManager manager;

		legIdInput = super.getRequest().getData("id", String.class);

		try {
			legId = Integer.parseInt(legIdInput);
			leg = this.repository.findFlightLegById(legId);
			managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			manager = this.repository.findManagerById(managerId);
			authorised = leg != null && leg.getParentFlight() != null && leg.getParentFlight().getManager() != null && leg.getParentFlight().getManager().equals(manager);
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		FlightLeg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findFlightLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final FlightLeg leg) {
		Dataset dataset;
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		SelectChoices statusChoices, arrivalAirportChoices, departureAirportChoices, aircraftChoices, flightChoices;
		List<SelectChoices> flightLegChoices = FlightHelper.getFlightLegFormChoices(leg, managerId);

		statusChoices = flightLegChoices.get(0);
		arrivalAirportChoices = flightLegChoices.get(1);
		departureAirportChoices = flightLegChoices.get(2);
		aircraftChoices = flightLegChoices.get(3);
		flightChoices = flightLegChoices.get(4);

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");

		dataset.put("parentFlights", flightChoices);
		dataset.put("parentFlight", flightChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("deployedAircrafts", aircraftChoices);
		dataset.put("deployedAircraft", aircraftChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
