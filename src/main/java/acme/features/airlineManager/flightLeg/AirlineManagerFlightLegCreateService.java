
package acme.features.airlineManager.flightLeg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight;
import acme.entities.FlightLeg;
import acme.helpers.FlightHelper;
import acme.helpers.ValidatorHelper;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegCreateService extends AbstractGuiService<AirlineManager, FlightLeg> {

	@Autowired
	private AirlineManagerFlightLegRepository repository;


	@Override
	public void authorise() {
		boolean authorised;
		try {
			String flightIdInput = super.getRequest().getData("parentId", String.class);
			int flightId = Integer.parseInt(flightIdInput);
			Flight flight = this.repository.findFlightById(flightId);

			authorised = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager());
		} catch (NumberFormatException e) {
			authorised = false;
		}

		authorised &= ValidatorHelper.isValidEntityReference("departureAirport", super.getRequest(), this.repository.findAllAirports());
		authorised &= ValidatorHelper.isValidEntityReference("arrivalAirport", super.getRequest(), this.repository.findAllAirports());
		authorised &= ValidatorHelper.isValidEntityReference("deployedAircraft", super.getRequest(), this.repository.findActiveAircrafts());

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		FlightLeg leg = new FlightLeg();
		leg.setDraftMode(true);

		int flightId = super.getRequest().getData("parentId", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		leg.setParentFlight(flight);

		super.getBuffer().addData(leg);
		super.getResponse().addGlobal("parentId", flightId);
	}

	@Override
	public void bind(final FlightLeg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "deployedAircraft");
	}

	@Override
	public void validate(final FlightLeg leg) {
		;
	}

	@Override
	public void perform(final FlightLeg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final FlightLeg leg) {
		Dataset dataset;
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		SelectChoices statusChoices, arrivalAirportChoices, departureAirportChoices, aircraftChoices;
		List<SelectChoices> flightLegChoices = FlightHelper.getFlightLegFormChoices(leg, managerId);

		statusChoices = flightLegChoices.get(0);
		arrivalAirportChoices = flightLegChoices.get(1);
		departureAirportChoices = flightLegChoices.get(2);
		aircraftChoices = flightLegChoices.get(3);

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");

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
