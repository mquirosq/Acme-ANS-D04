
package acme.features.administrator.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Service;

@GuiController
public class AdministratorServiceController extends AbstractGuiController<Administrator, Service> {

	@Autowired
	private AdministratorServiceListService		listService;

	@Autowired
	private AdministratorServiceShowService		showService;

	@Autowired
	private AdministratorServiceCreateService	createService;

	@Autowired
	private AdministratorServiceUpdateService	updateService;

	@Autowired
	private AdministratorServiceDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
	}

}
