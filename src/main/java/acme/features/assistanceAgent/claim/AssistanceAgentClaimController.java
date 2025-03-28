
package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Claim;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimListCompletedService	listCompletedService;

	@Autowired
	private AssistanceAgentClaimListPendingService		listPendingService;

	@Autowired
	private AssistanceAgentClaimShowService				showService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-pending", "list", this.listPendingService);

		super.addBasicCommand("show", this.showService);
	}

}
