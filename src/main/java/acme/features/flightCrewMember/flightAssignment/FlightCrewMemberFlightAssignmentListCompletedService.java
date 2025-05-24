
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.FlightLegStatus;
import acme.entities.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> completedFlightAssignments;

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		completedFlightAssignments = this.repository.findCompletedFlightAssignments(FlightLegStatus.LANDED, flightCrewMemberId);

		super.getBuffer().addData(completedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "allocatedFlightCrewMember.employeeCode", "leg.flightNumber");

		super.addPayload(dataset, flightAssignment, "remarks", "published");
		super.getResponse().addData(dataset);
	}

}
