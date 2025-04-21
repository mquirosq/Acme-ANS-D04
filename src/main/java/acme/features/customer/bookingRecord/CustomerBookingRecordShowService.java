
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
		Booking booking;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingOfBookingRecordById(id);
		authorised = booking != null && super.getRequest().getPrincipal().getActiveRealm().equals(booking.getCustomer());

		String passengerIdRaw;
		int passengerId;
		Passenger passenger;
		Collection<Passenger> legalPassengers;

		legalPassengers = this.repository.findMyPassengersNotAlreadyInBooking(super.getRequest().getPrincipal().getActiveRealm().getId(), id);

		if (super.getRequest().hasData("passenger")) {
			passengerIdRaw = super.getRequest().getData("passenger", String.class);

			try {
				passengerId = Integer.parseInt(passengerIdRaw);
			} catch (NumberFormatException e) {
				passengerId = -1;
				authorised = false;
			}

			if (passengerId != 0) {
				passenger = this.repository.findPassengerById(passengerId);
				authorised &= passenger != null && legalPassengers.contains(passenger);
			}
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
		int bookingId = super.getResponse().getData("bookingId", int.class);
		passengers = this.repository.findMyPassengersNotAlreadyInBooking(customerId, bookingId);
		choices = SelectChoices.from(passengers, "identifier", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord);
		dataset.put("passenger", bookingRecord.getPassenger().getId());
		dataset.put("passengers", choices);

		super.getResponse().addData(dataset);
	}
}
