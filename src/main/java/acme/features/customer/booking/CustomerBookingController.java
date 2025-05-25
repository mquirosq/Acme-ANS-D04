
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Booking;
import acme.realms.Customer;

@GuiController
public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	private final CustomerBookingListService	listService;

	private final CustomerBookingShowService	showService;

	private final CustomerBookingUpdateService	editService;

	private final CustomerBookingCreateService	createService;

	private final CustomerBookingPublishService	publishService;


	@Autowired
	public CustomerBookingController(final CustomerBookingListService listService, final CustomerBookingShowService showService, final CustomerBookingUpdateService editService, final CustomerBookingCreateService createService,
		final CustomerBookingPublishService publishService) {

		this.listService = listService;
		this.showService = showService;
		this.editService = editService;
		this.createService = createService;
		this.publishService = publishService;
	}

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.editService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
