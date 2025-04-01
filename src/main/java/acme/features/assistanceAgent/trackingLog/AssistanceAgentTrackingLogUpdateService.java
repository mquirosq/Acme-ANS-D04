
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
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int trackingLogId;
		TrackingLog trackingLog;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		authorised = trackingLog != null;

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;

		trackingLog = new TrackingLog();
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("claim", int.class);
		claim = this.repository.findClaimById(claimId);

		super.bindObject(trackingLog, "lastUpdateMoment", "creationMoment", "step", "resolutionPercentage", "resolution", "isPublished", "status");
		trackingLog.setClaim(claim);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		Collection<Claim> claims;
		SelectChoices statusChoices, claimChoices;

		claims = this.repository.findAllClaimsByAssistanceAgentId(trackingLog.getClaim().getAgent().getId());
		statusChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getStatus());
		claimChoices = SelectChoices.from(claims, "id", trackingLog.getClaim());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "creationMoment", "step", "resolutionPercentage", "resolution", "isPublished");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("claims", claimChoices);
		dataset.put("claim", claimChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
