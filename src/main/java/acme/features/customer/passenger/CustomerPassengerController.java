
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

	@Autowired
	private CustomerPassengerListService	listService;

	@Autowired
	private CustomerPassengerShowService	showService;

	@Autowired
	private CustomerPassengerUpdateService	editService;

	@Autowired
	private CustomerPassengerCreateService	createService;

	@Autowired
	private CustomerPassengerPublishService	publishService;

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
