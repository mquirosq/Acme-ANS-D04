
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.Claim;
import acme.entities.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int trackingLogId, agentId;
		TrackingLog trackingLog;
		AssistanceAgent agent;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);

		authorised = trackingLog != null && trackingLog.getClaim() != null && trackingLog.getClaim().getAgent() != null && trackingLog.getClaim().getAgent().equals(agent);

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id;
		TrackingLog trackingLog;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		Collection<Claim> claims;
		SelectChoices statusChoices, claimChoices;

		claims = this.repository.findAllClaimsByAssistanceAgentId(super.getRequest().getPrincipal().getActiveRealm().getId());
		statusChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getStatus());
		claimChoices = SelectChoices.from(claims, "id", trackingLog.getClaim());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "creationMoment", "step", "resolutionPercentage", "resolution", "isPublished");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("claims", claimChoices);
		dataset.put("claim", claimChoices.getSelected().getKey());

		if (trackingLog.getClaim() != null)
			super.getResponse().addGlobal("isClaimPublished", trackingLog.getClaim().getIsPublished());
		else
			super.getResponse().addGlobal("isClaimPublished", false);
		super.getResponse().addData(dataset);
	}
}
