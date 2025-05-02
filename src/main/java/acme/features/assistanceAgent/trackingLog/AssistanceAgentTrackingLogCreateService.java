
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.Claim;
import acme.entities.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int masterId;
		String masterIdRaw;
		Claim claim;

		claim = null;

		if (super.getRequest().hasData("masterId")) {
			masterIdRaw = super.getRequest().getData("masterId", String.class);

			try {
				masterId = Integer.parseInt(masterIdRaw);
			} catch (NumberFormatException e) {
				masterId = -1;
			}
			claim = this.repository.findClaimById(masterId);
		}
		authorised = claim != null && claim.getAgent() != null && super.getRequest().getPrincipal().hasRealm(claim.getAgent());
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
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		super.bindObject(trackingLog, "step", "resolutionPercentage", "resolution", "status");

		trackingLog.setIsPublished(false);
		trackingLog.setClaim(claim);
		trackingLog.setCreationMoment(MomentHelper.getCurrentMoment());
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
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
		int masterId;
		Dataset dataset;
		SelectChoices statusChoices;

		masterId = super.getRequest().getData("masterId", int.class);
		statusChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "creationMoment", "step", "resolutionPercentage", "resolution", "isPublished");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("masterId", masterId);
	}
}
