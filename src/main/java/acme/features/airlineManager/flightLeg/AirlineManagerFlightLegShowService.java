
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.FlightLegStatus;
import acme.entities.Aircraft;
import acme.entities.Airport;
import acme.entities.Flight;
import acme.entities.FlightLeg;
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
		} catch (NumberFormatException e) {
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
		Collection<Flight> flights;
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;

		SelectChoices statusChoices, arrivalAirportChoices, departureAirportChoices, aircraftChoices, flightChoices;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findAllFlightsByAirlineManagerId(managerId);
		airports = this.repository.findAllAirports();
		aircrafts = this.repository.findAllAircrafts();

		statusChoices = SelectChoices.from(FlightLegStatus.class, leg.getStatus());
		arrivalAirportChoices = SelectChoices.from(airports, "IATACode", leg.getArrivalAirport());
		departureAirportChoices = SelectChoices.from(airports, "IATACode", leg.getDepartureAirport());
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getDeployedAircraft());
		flightChoices = SelectChoices.from(flights, "tag", leg.getParentFlight());

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
