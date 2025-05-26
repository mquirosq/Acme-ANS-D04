
package acme.features.administrator.airport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Airport;

@GuiController
public class AdministratorAirportController extends AbstractGuiController<Administrator, Airport> {

	// Internal state ---------------------------------------------------------

	private final AdministratorAirportListService	listService;

	private final AdministratorAirportShowService	showService;

	private final AdministratorAirportUpdateService	updateService;

	private final AdministratorAirportCreateService	createService;


	@Autowired
	public AdministratorAirportController(final AdministratorAirportListService listService, final AdministratorAirportShowService showService, final AdministratorAirportUpdateService updateService, final AdministratorAirportCreateService createService) {
		this.listService = listService;
		this.showService = showService;
		this.createService = createService;
		this.updateService = updateService;
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
