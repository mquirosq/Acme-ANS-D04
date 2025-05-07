
package acme.features.administrator.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TravelClass;
import acme.entities.Booking;
import acme.entities.Flight;
import acme.realms.Customer;

@GuiService
public class AdministratorBookingShowService extends AbstractGuiService<Administrator, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Boolean authorised;
		String rawId;
		int bookingId;
		Booking booking;

		try {
			rawId = super.getRequest().getData("id", String.class);
			bookingId = Integer.parseInt(rawId);
			booking = this.repository.findBookingById(bookingId);
			authorised = booking != null && !booking.isDraftMode();
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> flights;
		Collection<Customer> customers;
		SelectChoices flightChoices;
		SelectChoices travelChoices;
		SelectChoices customerChoices;
		Dataset dataset;
		Date currentMoment;

		currentMoment = MomentHelper.getCurrentMoment();
		flights = this.repository.findAllNonDraftFlights();
		flights = flights.stream().filter(f -> (f.getScheduledDeparture() != null && MomentHelper.isAfter(f.getScheduledDeparture(), currentMoment))).toList();

		flightChoices = SelectChoices.from(flights, "identifierCode", booking.getFlight());
		travelChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		customers = this.repository.findAllCustomers();

		customerChoices = SelectChoices.from(customers, "identifier", booking.getCustomer());

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "lastCardNibble", "price", "purchasedAt");
		dataset.put("customer", customerChoices.getSelected().getKey());
		dataset.put("customers", customerChoices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("travelClass", travelChoices.getSelected().getKey());
		dataset.put("travelClasses", travelChoices);

		super.getResponse().addData(dataset);
	}

}
