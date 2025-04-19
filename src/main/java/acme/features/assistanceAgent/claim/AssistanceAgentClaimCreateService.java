
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.datatypes.ClaimType;
import acme.entities.Claim;
import acme.entities.FlightLeg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int legId;
		String legIdRaw;
		FlightLeg leg;

		authorised = true;

		if (super.getRequest().hasData("leg")) {
			legIdRaw = super.getRequest().getData("leg", String.class);

			try {
				legId = Integer.parseInt(legIdRaw);
			} catch (Throwable e) {
				legId = -1;
				authorised = false;
			}

			if (legId != 0) {
				leg = this.repository.findLegById(legId);
				authorised &= leg != null;
			}
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Claim claim;

		claim = new Claim();
		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId, agentId;
		FlightLeg leg;
		AssistanceAgent agent;

		legId = super.getRequest().getData("leg", int.class);
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		leg = this.repository.findLegById(legId);
		agent = this.repository.findAssistanceAgentById(agentId);

		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setIsPublished(false);
		claim.setLeg(leg);
		claim.setAgent(agent);
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final Claim claim) {

	}

	@Override
	public void perform(final Claim claim) {
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
		dataset.put("readonly", false);
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
