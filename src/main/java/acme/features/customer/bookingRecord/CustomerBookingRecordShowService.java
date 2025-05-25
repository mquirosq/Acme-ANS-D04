
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Booking;
import acme.entities.BookingRecord;
import acme.entities.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {

	private final CustomerBookingRecordRepository repository;


	@Autowired
	public CustomerBookingRecordShowService(final CustomerBookingRecordRepository repository) {
		this.repository = repository;
	}

	@Override
	public void authorise() {
		boolean authorised;
		int id;
		String rawId;
		Booking booking;
		BookingRecord bookingRecord;

		try {
			rawId = super.getRequest().getData("id", String.class);
			id = Integer.parseInt(rawId);
			bookingRecord = this.repository.findBookingRecordById(id);
			booking = this.repository.findBookingOfBookingRecordById(id);
			// For coverage, the second part of the condition is being considered as multiple parts. 
			// However, a test has been made for a booking record that is not the principal's.
			authorised = bookingRecord != null && super.getRequest().getPrincipal().getActiveRealm().equals(booking.getCustomer());
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		int id;
		boolean isDraft;

		id = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.findBookingRecordById(id);
		isDraft = bookingRecord.getBooking().isDraftMode();

		super.getBuffer().addData(bookingRecord);
		super.getResponse().addGlobal("draft", isDraft);
		super.getResponse().addGlobal("bookingId", bookingRecord.getBooking().getId());
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Collection<Passenger> passengers;
		SelectChoices choices;
		Dataset dataset;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		passengers = this.repository.findMyPassengers(customerId);
		choices = SelectChoices.from(passengers, "identifier", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord);
		dataset.put("passenger", bookingRecord.getPassenger().getId());
		dataset.put("passengers", choices);

		super.getResponse().addData(dataset);
	}
}
