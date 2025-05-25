
package acme.features.customer.bookingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.BookingRecord;
import acme.realms.Customer;

@GuiController
public class CustomerBookingRecordController extends AbstractGuiController<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	private final CustomerBookingRecordListService		listService;

	private final CustomerBookingRecordShowService		showService;

	private final CustomerBookingRecordCreateService	createService;

	private final CustomerBookingRecordDeleteService	deleteService;


	@Autowired
	public CustomerBookingRecordController(final CustomerBookingRecordListService listService, final CustomerBookingRecordShowService showService, final CustomerBookingRecordCreateService createService,
		final CustomerBookingRecordDeleteService deleteService) {

		this.listService = listService;
		this.showService = showService;
		this.createService = createService;
		this.deleteService = deleteService;

	}

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
