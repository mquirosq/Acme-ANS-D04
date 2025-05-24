
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight;
import acme.entities.FlightLeg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegListService extends AbstractGuiService<AirlineManager, FlightLeg> {

	@Autowired
	private AirlineManagerFlightLegRepository repository;


	@Override
	public void authorise() {
		Boolean authorised = true;

		try {
			String flightIdInput = super.getRequest().getData("parentId", String.class);
			int flightId = Integer.parseInt(flightIdInput);
			Flight flight = this.repository.findFlightById(flightId);
			authorised = flight != null && super.getRequest().getPrincipal().getActiveRealm().equals(flight.getManager());
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int flightId;
		Collection<FlightLeg> legs;

		flightId = super.getRequest().getData("parentId", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		legs = this.repository.findAllLegsByFlightId(flightId);

		super.getBuffer().addData(legs);
		super.getResponse().addGlobal("parentId", flightId);
		super.getResponse().addGlobal("parentDraftMode", flight.getDraftMode());
	}

	@Override
	public void unbind(final FlightLeg leg) {
		String departureAirport = "";
		String arrivalAirport = "";
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "draftMode");

		departureAirport = leg.getDepartureAirport().getIATACode();
		arrivalAirport = leg.getArrivalAirport().getIATACode();

		dataset.put("departureAirport", departureAirport);
		dataset.put("arrivalAirport", arrivalAirport);

		super.addPayload(dataset, leg, "scheduledDeparture", "scheduledArrival", "status");

		super.getResponse().addData(dataset);
	}

}
