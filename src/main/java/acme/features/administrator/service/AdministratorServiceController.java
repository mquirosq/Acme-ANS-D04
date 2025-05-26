
package acme.features.administrator.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Service;

@GuiController
public class AdministratorServiceController extends AbstractGuiController<Administrator, Service> {

	private final AdministratorServiceListService	listService;

	private final AdministratorServiceShowService	showService;

	private final AdministratorServiceCreateService	createService;

	private final AdministratorServiceUpdateService	updateService;

	private final AdministratorServiceDeleteService	deleteService;


	@Autowired
	public AdministratorServiceController(final AdministratorServiceListService listService, final AdministratorServiceShowService showService, final AdministratorServiceCreateService createService, final AdministratorServiceUpdateService updateService,
		final AdministratorServiceDeleteService deleteService) {

		this.listService = listService;
		this.showService = showService;
		this.createService = createService;
		this.updateService = updateService;
		this.deleteService = deleteService;

	}

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
	}

}
