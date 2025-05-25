
package acme.features.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Passenger;
import acme.realms.Customer;

@GuiController
public class CustomerPassengerController extends AbstractGuiController<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	private final CustomerPassengerListService		listService;

	private final CustomerPassengerShowService		showService;

	private final CustomerPassengerUpdateService	editService;

	private final CustomerPassengerCreateService	createService;

	private final CustomerPassengerPublishService	publishService;


	@Autowired
	public CustomerPassengerController(final CustomerPassengerListService listService, final CustomerPassengerShowService showService, final CustomerPassengerUpdateService editService, final CustomerPassengerCreateService createService,
		final CustomerPassengerPublishService publishService) {

		this.listService = listService;
		this.showService = showService;
		this.editService = editService;
		this.createService = createService;
		this.publishService = publishService;

	}

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.editService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
