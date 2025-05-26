
package acme.features.authenticated.technician;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.Technician;

@GuiController
public class AuthenticatedTechnicianController extends AbstractGuiController<Authenticated, Technician> {

	private final AuthenticatedTechnicianCreateService	createService;

	private final AuthenticatedTechnicianUpdateService	updateService;


	@Autowired
	public AuthenticatedTechnicianController(final AuthenticatedTechnicianCreateService createService, final AuthenticatedTechnicianUpdateService updateService) {
		this.createService = createService;
		this.updateService = updateService;
	}

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}
}
