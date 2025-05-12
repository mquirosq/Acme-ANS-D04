
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.ActivityLog;
import acme.realms.FlightCrewMember;

@GuiService
public class ActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised = true;

		int activityLogId;
		String requestActivityLogId;
		ActivityLog activityLog;

		if (super.getRequest().hasData("id")) {
			requestActivityLogId = super.getRequest().getData("id", String.class);
			try {
				activityLogId = Integer.parseInt(requestActivityLogId);
				activityLog = this.repository.findActivityLogById(activityLogId);
				authorised = activityLog != null && activityLog.getAssignment() != null && super.getRequest().getPrincipal().hasRealm(activityLog.getAssignment().getAllocatedFlightCrewMember());
			} catch (NumberFormatException e) {
				authorised = false;
			}
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int activityLogId;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "published");
		dataset.put("readonly", false);
		super.getResponse().addData(dataset);

	}

}
