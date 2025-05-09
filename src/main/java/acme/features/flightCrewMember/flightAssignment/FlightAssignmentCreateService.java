
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		boolean authorised = true;

		int flightLegId;
		String requestFlightLegId;
		FlightLeg leg;

		int flightCrewMemberId;
		String requestFlightCrewMemberId;
		FlightCrewMember flightCrewMember;

		if (super.getRequest().hasData("leg") && super.getRequest().hasData("allocatedFlightCrewMember")) {
			requestFlightLegId = super.getRequest().getData("leg", String.class);
			requestFlightCrewMemberId = super.getRequest().getData("allocatedFlightCrewMember", String.class);
			try {
				flightLegId = Integer.parseInt(requestFlightLegId);
				flightCrewMemberId = Integer.parseInt(requestFlightCrewMemberId);
				leg = this.repository.findByLegId(flightLegId);
				flightCrewMember = this.repository.findByFlightCrewMemberId(flightCrewMemberId);
				authorised = leg != null && flightCrewMember != null;
			} catch (NumberFormatException e) {
				authorised = false;
			}
		}
		super.getResponse().setAuthorised(authorised);
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

		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks");
		flightAssignment.setLeg(flightLeg);
		flightAssignment.setAllocatedFlightCrewMember(flightCrewMember);
		flightAssignment.setPublished(false);
		flightAssignment.setMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
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
