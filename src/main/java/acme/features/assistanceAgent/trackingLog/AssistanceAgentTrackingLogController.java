
package acme.features.assistanceAgent.trackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentTrackingLogController extends AbstractGuiController<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogListService	listService;

	@Autowired
	private AssistanceAgentTrackingLogShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
