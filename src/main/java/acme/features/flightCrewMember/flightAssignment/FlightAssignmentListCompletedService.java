
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentListCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> completedFlightAssignments;
		Date currentMoment;

		currentMoment = MomentHelper.getCurrentMoment();
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		completedFlightAssignments = this.repository.findCompletedFlightAssignments(currentMoment, flightCrewMemberId);

		super.getBuffer().addData(completedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "allocatedFlightCrewMember.employeeCode", "leg.flightNumber");

		super.addPayload(dataset, flightAssignment, "remarks");
		super.getResponse().addData(dataset);
	}

}
