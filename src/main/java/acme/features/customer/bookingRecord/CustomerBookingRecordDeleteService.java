
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.BookingRecord;
import acme.entities.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean authorised;

		int bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.repository.findBookingRecordById(bookingRecordId);

		Customer customer = bookingRecord.getBooking().getCustomer();

		authorised = bookingRecord != null && bookingRecord.getBooking() != null && bookingRecord.getBooking().isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.findBookingRecordById(id);

		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		;
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.delete(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Collection<Passenger> passengers;
		SelectChoices choices;
		Dataset dataset;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = bookingRecord.getBooking().getId();
		passengers = this.repository.findMyPassengersNotAlreadyInBooking(customerId, bookingId);
		choices = SelectChoices.from(passengers, "identifier", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord);
		dataset.put("passenger", bookingRecord.getPassenger().getId());
		dataset.put("passengers", choices);

		super.getResponse().addData(dataset);
	}
}
