
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

	@Autowired
	private CustomerBookingRecordRepository repository;


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
			authorised = bookingRecord != null && booking != null && super.getRequest().getPrincipal().getActiveRealm().equals(booking.getCustomer());
		} catch (NumberFormatException e) {
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
		if (bookingRecord.getBooking() != null)
			isDraft = bookingRecord.getBooking().isDraftMode();
		else
			isDraft = false;

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
