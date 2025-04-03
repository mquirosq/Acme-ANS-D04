
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
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();
		flightAssignment.setPublished(false);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		int flightLegId;
		FlightLeg flightLeg;

		int flightCrewMemberId;
		FlightCrewMember flightCrewMember;

		flightLegId = super.getRequest().getData("leg", int.class);
		flightLeg = this.repository.findByLegId(flightLegId);

		flightCrewMemberId = super.getRequest().getData("allocatedFlightCrewMember", int.class);
		flightCrewMember = this.repository.findByFlightCrewMemberId(flightCrewMemberId);

		super.bindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "published");
		flightAssignment.setLeg(flightLeg);
		flightAssignment.setAllocatedFlightCrewMember(flightCrewMember);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		FlightCrewMember fcm = flightAssignment.getAllocatedFlightCrewMember();
		Collection<FlightAssignment> flightLegsForFlightCrewMember = this.repository.findFlightAssignmentsByFlightCrewMemberId(fcm.getId());
		Boolean check = true;
		for (FlightAssignment fa : flightLegsForFlightCrewMember) {
			check = !MomentHelper.isInRange(flightAssignment.getLeg().getScheduledDeparture(), fa.getLeg().getScheduledDeparture(), fa.getLeg().getScheduledArrival())
				|| !MomentHelper.isInRange(flightAssignment.getLeg().getScheduledArrival(), fa.getLeg().getScheduledDeparture(), fa.getLeg().getScheduledArrival());
			super.state(check, "*", "acme.validation.flightAssignment.overlappingLegs.message");
		}

		FlightLeg fl = flightAssignment.getLeg();
		boolean pastLeg = MomentHelper.isBefore(fl.getScheduledDeparture(), MomentHelper.getCurrentMoment());
		super.state(!pastLeg, "*", "acme.validation.flightAssignment.pastLeg.message");

		Collection<FlightAssignment> flightAssignmentsForTheLeg = this.repository.findFlightAssignmentsByFlightLegId(fl.getId());
		boolean copilot = false;
		boolean pilot = false;
		for (FlightAssignment fa : flightAssignmentsForTheLeg) {
			if (fa.getDuty().equals(Duty.PILOT))
				pilot = true;
			if (fa.getDuty().equals(Duty.COPILOT))
				copilot = true;
		}

		boolean extraPilot = pilot && flightAssignment.getDuty().equals(Duty.PILOT);
		boolean extraCopilot = copilot && flightAssignment.getDuty().equals(Duty.COPILOT);
		super.state(!extraPilot, "*", "acme.validation.flightAssignment.extraPilot.message");
		super.state(!extraCopilot, "*", "acme.validation.flightAssignment.extraCopilot.message");

		boolean flightCrewMemberAvailable = flightAssignment.getAllocatedFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
		super.state(flightCrewMemberAvailable, "*", "acme.validation.flightAssignment.unavailableFlightCrewMember.message");

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		SelectChoices legChoices, flightCrewMemberChoices, statusChoices, dutyChoices;

		Collection<FlightLeg> flightLegs = this.repository.findAllLegs();
		Collection<FlightCrewMember> flightCrewMembers = this.repository.findAllFlightCrewMembers();

		legChoices = SelectChoices.from(flightLegs, "flightNumber", flightAssignment.getLeg());
		flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "employeeCode", flightAssignment.getAllocatedFlightCrewMember());
		statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "moment", "remarks", "published");
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", flightCrewMemberChoices);
		dataset.put("allocatedFlightCrewMember", flightCrewMemberChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("statusChoices", statusChoices);
		dataset.put("currentStatus", statusChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
