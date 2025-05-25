
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.Claim;
import acme.entities.TrackingLog;
import acme.helpers.InternationalisationHelper;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		int masterId;
		Collection<TrackingLog> trackingLogs;

		masterId = super.getRequest().getData("masterId", int.class);
		trackingLogs = this.repository.findAllTrackingLogsByMasterId(masterId);
		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "resolutionPercentage", "status");
		dataset.put("isPublished", InternationalisationHelper.internationalizeBoolean(trackingLog.getIsPublished()));

		super.addPayload(dataset, trackingLog, "lastUpdateMoment", "creationMoment", "resolution", "step", "claim.id");
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> trackingLogs) {
		int masterId;
		boolean canCreate;

		canCreate = trackingLogs.stream().filter(t -> !t.getStatus().equals(ClaimStatus.PENDING)).count() < 2L;
		masterId = super.getRequest().getData("masterId", int.class);

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("canCreate", canCreate);
	}
}
