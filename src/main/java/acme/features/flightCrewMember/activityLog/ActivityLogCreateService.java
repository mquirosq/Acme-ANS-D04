
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.ActivityLog;
import acme.entities.FlightAssignment;
import acme.features.flightCrewMember.flightAssignment.FlightAssignmentRepository;
import acme.realms.FlightCrewMember;

@GuiService
public class ActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	ActivityLogRepository				repository;

	@Autowired
	private FlightAssignmentRepository	flightAssignmentRepository;


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
		ActivityLog activityLog = new ActivityLog();
		activityLog.setPublished(false);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		int requestFlightAssignmentId = super.getRequest().getData("masterId", int.class);
		FlightAssignment flightAssignment = this.flightAssignmentRepository.findFlightAssignmentById(requestFlightAssignmentId);

		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
		activityLog.setAssignment(flightAssignment);
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		activityLog.setPublished(false);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		;
	}
}
