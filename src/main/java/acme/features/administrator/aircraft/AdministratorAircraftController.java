
package acme.features.administrator.aircraft;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Aircraft;

@GuiController
public class AdministratorAircraftController extends AbstractGuiController<Administrator, Aircraft> {

	private final AdministratorAircraftListService		listService;

	private final AdministratorAircraftShowService		showService;

	private final AdministratorAircraftCreateService	createService;

	private final AdministratorAircraftUpdateService	updateService;

	private final AdministratorAircraftDisableService	disableService;


	@Autowired
	public AdministratorAircraftController(final AdministratorAircraftListService listService, final AdministratorAircraftShowService showService, final AdministratorAircraftCreateService createService,
		final AdministratorAircraftUpdateService updateService, final AdministratorAircraftDisableService disableService) {
		this.listService = listService;
		this.showService = showService;
		this.createService = createService;
		this.updateService = updateService;
		this.disableService = disableService;
	}

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("disable", "update", this.disableService);
	}
}
