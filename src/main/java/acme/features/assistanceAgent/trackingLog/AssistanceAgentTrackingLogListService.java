
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int assistanceAgentId;
		Collection<TrackingLog> trackingLogs;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		trackingLogs = this.repository.findAllTrackingLogsByAssistanceAgentId(assistanceAgentId);
		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "resolutionPercentage", "status", "isPublished");

		super.addPayload(dataset, trackingLog, "creationMoment", "resolution", "step", "claim.id");
		super.getResponse().addData(dataset);
	}
}
