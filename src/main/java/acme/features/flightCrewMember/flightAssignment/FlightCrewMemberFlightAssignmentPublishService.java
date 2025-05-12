
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AvailabilityStatus;
import acme.datatypes.CurrentStatus;
import acme.datatypes.Duty;
import acme.datatypes.FlightLegStatus;
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean authorised = true;

		int flightLegId;
		String requestFlightLegId;
		FlightLeg leg;

		int flightAssignmentId;
		String requestFlightAssignmentId;
		FlightAssignment flightAssignment;

		if (super.getRequest().hasData("leg") && super.getRequest().hasData("id")) {
			requestFlightLegId = super.getRequest().getData("leg", String.class);
			requestFlightAssignmentId = super.getRequest().getData("id", String.class);
			try {
				flightLegId = Integer.parseInt(requestFlightLegId);
				flightAssignmentId = Integer.parseInt(requestFlightAssignmentId);
				leg = this.repository.findByLegId(flightLegId);
				flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

				authorised = leg != null && flightAssignment != null && !flightAssignment.getPublished() && flightAssignment.getAllocatedFlightCrewMember() != null
					&& super.getRequest().getPrincipal().hasRealm(flightAssignment.getAllocatedFlightCrewMember());
			} catch (NumberFormatException e) {
				authorised = false;
			}
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		boolean legHasOccured = flightAssignment.getLeg().getStatus().equals(FlightLegStatus.LANDED);
		super.state(!legHasOccured, "leg", "acme.validation.flightAssignment.pastLeg.message");

		boolean flightCrewMemberIsAvailable = flightAssignment.getAllocatedFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
		super.state(flightCrewMemberIsAvailable, "*", "acme.validation.flightAssignment.unavailableFlightCrewMember.message");

		FlightCrewMember fcm = flightAssignment.getAllocatedFlightCrewMember();
		Collection<FlightAssignment> flightAssignmentsForFlightCrewMember = this.repository.findFlightAssignmentsByFlightCrewMemberId(fcm.getId());
		flightAssignmentsForFlightCrewMember.remove(flightAssignment);

		boolean simultaneousLegs = flightAssignmentsForFlightCrewMember.stream()
			.anyMatch(x -> MomentHelper.isBefore(x.getLeg().getScheduledDeparture(), flightAssignment.getLeg().getScheduledArrival()) && MomentHelper.isBefore(flightAssignment.getLeg().getScheduledDeparture(), x.getLeg().getScheduledArrival()));
		super.state(!simultaneousLegs, "leg", "acme.validation.flightAssignment.overlappingLegs.message");

		Collection<FlightAssignment> flightAssignmentsForTheLeg = this.repository.findFlightAssignmentsByFlightLegId(flightAssignment.getLeg().getId());
		boolean pilot = false;
		boolean copilot = false;

		pilot = flightAssignmentsForTheLeg.stream().anyMatch(x -> x.getDuty().equals(Duty.PILOT));
		copilot = flightAssignmentsForTheLeg.stream().anyMatch(x -> x.getDuty().equals(Duty.COPILOT));

		boolean extraPilot = pilot && flightAssignment.getDuty().equals(Duty.PILOT);
		boolean extraCopilot = copilot && flightAssignment.getDuty().equals(Duty.COPILOT);
		super.state(!extraPilot, "duty", "acme.validation.flightAssignment.extraPilot.message");
		super.state(!extraCopilot, "duty", "acme.validation.flightAssignment.extraCopilot.message");

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setPublished(true);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		SelectChoices legChoices, statusChoices, dutyChoices;

		Collection<FlightLeg> flightLegs = this.repository.findAllLegs();

		legChoices = SelectChoices.from(flightLegs, "flightNumber", flightAssignment.getLeg());
		statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "moment", "remarks", "published");
		dataset.put("readonly", false);
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("statusChoices", statusChoices);
		dataset.put("currentStatus", statusChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
