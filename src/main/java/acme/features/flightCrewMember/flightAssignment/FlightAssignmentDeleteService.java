
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.CurrentStatus;
import acme.datatypes.Duty;
import acme.entities.ActivityLog;
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
import acme.features.flightCrewMember.activityLog.ActivityLogRepository;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	FlightAssignmentRepository	repository;

	@Autowired
	ActivityLogRepository		activityLogRepository;


	@Override
	public void authorise() {
		boolean authorised = true;

		int requesterId;

		int flightLegId;
		String requestFlightLegId;
		FlightLeg leg;

		int flightCrewMemberId;
		String requestFlightCrewMemberId;
		FlightCrewMember flightCrewMember;

		int flightAssignmentId;
		String requestFlightAssignmentId;
		FlightAssignment flightAssignment;

		if (super.getRequest().hasData("leg") && super.getRequest().hasData("allocatedFlightCrewMember") && super.getRequest().hasData("id")) {
			requestFlightLegId = super.getRequest().getData("leg", String.class);
			requestFlightCrewMemberId = super.getRequest().getData("allocatedFlightCrewMember", String.class);
			requestFlightAssignmentId = super.getRequest().getData("id", String.class);
			try {
				flightLegId = Integer.parseInt(requestFlightLegId);
				flightCrewMemberId = Integer.parseInt(requestFlightCrewMemberId);
				flightAssignmentId = Integer.parseInt(requestFlightAssignmentId);
				leg = this.repository.findByLegId(flightLegId);
				flightCrewMember = this.repository.findByFlightCrewMemberId(flightCrewMemberId);
				flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
				requesterId = super.getRequest().getPrincipal().getActiveRealm().getId();
				authorised = leg != null && flightCrewMember != null && flightCrewMember.getId() == requesterId && flightAssignment != null && !flightAssignment.getPublished();
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
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Collection<ActivityLog> activityLogs = this.repository.findActivityLogsByFlightAssignmentId(flightAssignment.getId());
		this.activityLogRepository.deleteAll(activityLogs);
		this.repository.delete(flightAssignment);
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

		dataset = super.unbindObject(flightAssignment, "remarks", "moment", "published");
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
