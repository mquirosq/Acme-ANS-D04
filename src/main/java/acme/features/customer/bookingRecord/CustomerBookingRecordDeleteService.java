
package acme.features.customer.bookingRecord;

import java.util.Collection;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Booking;
import acme.entities.BookingRecord;
import acme.entities.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	private final CustomerBookingRecordRepository repository;


	public CustomerBookingRecordDeleteService(final CustomerBookingRecordRepository repository) {
		this.repository = repository;
	}

	// AbstractGuiService interface -------------------------------------------

	@Override
	public void authorise() {
		boolean authorised;
		int id;
		String rawId;
		BookingRecord bookingRecord;

		try {
			rawId = super.getRequest().getData("id", String.class);
			id = Integer.parseInt(rawId);
			bookingRecord = this.repository.findBookingRecordById(id);
			authorised = bookingRecord != null;
			if (authorised) {
				Customer customer = bookingRecord.getBooking().getCustomer();
				authorised &= bookingRecord.getBooking().isDraftMode() && super.getRequest().getPrincipal().getActiveRealm().equals(customer);
			}
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}

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
		// Intentionally left empty: no extra validation needed for BookingRecord in this context.
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.delete(bookingRecord);

		Booking booking = bookingRecord.getBooking();
		Money price = booking.getPrice();
		price.setAmount(price.getAmount() - booking.getFlight().getCost().getAmount());
		booking.setPrice(price);
		this.repository.save(booking);
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

		super.getResponse().addGlobal("draft", bookingRecord.getBooking().isDraftMode());

		super.getResponse().addData(dataset);
	}
}
