
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.ActivityLog;
import acme.entities.FlightAssignment;
import acme.features.flightCrewMember.flightAssignment.FlightCrewMemberFlightAssignmentRepository;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository		repository;

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository	flightAssignmentRepository;


	@Override
	public void authorise() {
		boolean authorised = true;

		int flightAssignmentId;
		String requestFlightAssignmentId;
		FlightAssignment flightAssignment;

		if (super.getRequest().hasData("masterId")) {
			requestFlightAssignmentId = super.getRequest().getData("masterId", String.class);
			try {
				flightAssignmentId = Integer.parseInt(requestFlightAssignmentId);
				flightAssignment = this.flightAssignmentRepository.findFlightAssignmentById(flightAssignmentId);
				authorised = flightAssignment != null && flightAssignment.getAllocatedFlightCrewMember() != null && super.getRequest().getPrincipal().hasRealm(flightAssignment.getAllocatedFlightCrewMember());
			} catch (NumberFormatException e) {
				authorised = false;
			}
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Collection<ActivityLog> activityLogs;
		String requestFlightAssignmentId = super.getRequest().getData("masterId", String.class);
		int flightAssignmentId = Integer.parseInt(requestFlightAssignmentId);
		activityLogs = this.repository.findByFlightAssignmentId(flightAssignmentId);

		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		dataset = super.unbindObject(activityLog, "typeOfIncident", "registrationMoment", "severityLevel");
		int masterId = super.getRequest().getData("masterId", int.class);
		super.addPayload(dataset, activityLog, "description");
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("masterId", masterId);
	}

}
