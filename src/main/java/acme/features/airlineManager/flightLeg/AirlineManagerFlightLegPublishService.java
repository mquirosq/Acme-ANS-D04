
package acme.features.airlineManager.flightLeg;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.FlightLeg;
import acme.helpers.FlightHelper;
import acme.helpers.ValidatorHelper;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegPublishService extends AbstractGuiService<AirlineManager, FlightLeg> {

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
			authorised = leg != null && leg.getDraftMode() && leg.getParentFlight() != null && leg.getParentFlight().getManager() != null && leg.getParentFlight().getManager().equals(manager);
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
		FlightLeg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findFlightLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final FlightLeg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "deployedAircraft");
	}

	@Override
	public void validate(final FlightLeg leg) {
		boolean departureIsInTheFuture = leg.getScheduledDeparture() != null && MomentHelper.isFuture(leg.getScheduledDeparture());

		super.state(departureIsInTheFuture, "scheduledDeparture", "acme.validation.flightLeg.scheduledDeparture.message");

		boolean publishedLegsNotOverlapping = true;

		List<FlightLeg> legsOfFlight = this.repository.getPublishedLegsOfFlightOrderedByArrival(leg.getParentFlight().getId());

		if (legsOfFlight.size() > 0)
			for (Integer i = 0; i < legsOfFlight.size(); i++) {
				FlightLeg legInList = legsOfFlight.get(i);
				Date scheduledArrival = legInList.getScheduledArrival();
				Date scheduledDeparture = legInList.getScheduledDeparture();
				if (MomentHelper.isBefore(scheduledDeparture, leg.getScheduledDeparture()))
					publishedLegsNotOverlapping &= MomentHelper.isBefore(scheduledArrival, leg.getScheduledDeparture());
				else if (MomentHelper.isAfter(scheduledArrival, leg.getScheduledArrival()))
					publishedLegsNotOverlapping &= MomentHelper.isAfter(scheduledDeparture, leg.getScheduledArrival());
				else
					publishedLegsNotOverlapping = false;
			}

		super.state(publishedLegsNotOverlapping, "scheduledDeparture", "acme.validation.flightLeg.overlappingDates.message");
		super.state(publishedLegsNotOverlapping, "scheduledArrival", "acme.validation.flightLeg.overlappingDates.message");
	}

	@Override
	public void perform(final FlightLeg leg) {
		leg.setDraftMode(false);
		this.repository.save(leg);
	}

	@Override
	public void unbind(final FlightLeg leg) {
		Dataset dataset;
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		SelectChoices statusChoices, arrivalAirportChoices, departureAirportChoices, aircraftChoices, parentFlightChoices;
		List<SelectChoices> flightLegChoices = FlightHelper.getFlightLegFormChoices(leg, managerId);

		statusChoices = flightLegChoices.get(0);
		arrivalAirportChoices = flightLegChoices.get(1);
		departureAirportChoices = flightLegChoices.get(2);
		aircraftChoices = flightLegChoices.get(3);
		parentFlightChoices = flightLegChoices.get(4);

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");

		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("deployedAircrafts", aircraftChoices);
		dataset.put("deployedAircraft", aircraftChoices.getSelected().getKey());
		dataset.put("parentFlights", parentFlightChoices);
		dataset.put("parentFlight", parentFlightChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
