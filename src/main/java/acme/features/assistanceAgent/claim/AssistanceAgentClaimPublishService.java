
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.datatypes.ClaimType;
import acme.entities.Claim;
import acme.entities.FlightLeg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int claimId, agentId;
		String claimIdRaw;

		Claim claim;
		AssistanceAgent agent;

		authorised = true;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);

		if (super.getRequest().hasData("id")) {
			claimIdRaw = super.getRequest().getData("id", String.class);

			try {
				claimId = Integer.parseInt(claimIdRaw);
			} catch (Throwable e) {
				claimId = -1;
				authorised = false;
			}
			claim = this.repository.findClaimById(claimId);
			authorised &= claim != null && !claim.getIsPublished() && claim.getAgent() != null && claim.getAgent().equals(agent) && !claim.getStatus().equals(ClaimStatus.PENDING);
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id;
		Claim claim;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {

	}

	@Override
	public void validate(final Claim claim) {

	}

	@Override
	public void perform(final Claim claim) {
		claim.setIsPublished(true);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices typeChoices, statusChoices, legChoices;
		Collection<FlightLeg> legs;

		legs = this.repository.findAllPublishedLegs();

		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		statusChoices = SelectChoices.from(ClaimStatus.class, claim.getStatus());
		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "isPublished");
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
