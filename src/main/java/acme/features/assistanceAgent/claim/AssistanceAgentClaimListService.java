
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int assistanceAgentId;
		Collection<Claim> claims;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findAllClaimsByAssistanceAgentId(assistanceAgentId);
		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		ClaimStatus status;

		status = claim.getStatus();
		dataset = super.unbindObject(claim, "registrationMoment", "isPublished", "type");
		dataset.put("status", claim.getStatus());

		super.addPayload(dataset, claim, "agent.employeeCode", "leg.flightNumber", "description", "passengerEmail");

		if (!status.equals(ClaimStatus.NO_STATUS) && !status.equals(ClaimStatus.PENDING))
			dataset.put("payload", dataset.get("payload") + "|completed");
		super.getResponse().addData(dataset);
	}

}
