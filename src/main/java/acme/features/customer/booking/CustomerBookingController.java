
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Booking;
import acme.realms.Customer;

@GuiController
public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	@Autowired
	private CustomerBookingListService		listService;

	@Autowired
	private CustomerBookingShowService		showService;

	@Autowired
	private CustomerBookingUpdateService	editService;

	@Autowired
	private CustomerBookingCreateService	createService;

	@Autowired
	private CustomerBookingPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.editService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
