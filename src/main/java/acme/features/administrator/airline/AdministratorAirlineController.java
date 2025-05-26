
package acme.features.administrator.airline;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Airline;

@GuiController
public class AdministratorAirlineController extends AbstractGuiController<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	private final AdministratorAirlineListService	listService;

	private final AdministratorAirlineShowService	showService;

	private final AdministratorAirlineUpdateService	updateService;

	private final AdministratorAirlineCreateService	createService;


	@Autowired
	public AdministratorAirlineController(final AdministratorAirlineListService listService, final AdministratorAirlineShowService showService, final AdministratorAirlineUpdateService updateService, final AdministratorAirlineCreateService createService) {
		this.listService = listService;
		this.showService = showService;
		this.updateService = updateService;
		this.createService = createService;
	}

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
	}

}
