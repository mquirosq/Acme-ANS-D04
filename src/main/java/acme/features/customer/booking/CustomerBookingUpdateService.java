
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TravelClass;
import acme.entities.Booking;
import acme.entities.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractService<Customer, Booking> -------------------------------------


	@Override
	public void authorise() {
		boolean authorised;

		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		Customer customer = booking.getCustomer();

		authorised = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

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
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchasedAt", "travelClass", "price", "lastCardNibble", "draftMode", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		Money price;
		Long numberPassengers;

		numberPassengers = this.repository.countPassengersInBooking(booking.getId());

		price = new Money();
		if (booking.getFlight() != null && booking.getFlight().getCost() != null) {
			price.setAmount(booking.getFlight().getCost().getAmount() * numberPassengers);
			price.setCurrency(booking.getFlight().getCost().getCurrency());
		} else
			price = null;

		booking.setPrice(price);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> flights;
		SelectChoices flightChoices;
		SelectChoices travelChoices;
		Dataset dataset;
		Date currentMoment;

		currentMoment = MomentHelper.getCurrentMoment();
		flights = this.repository.findAllNonDraftFlights();
		flights = flights.stream().filter(f -> (f.getScheduledDeparture() != null && MomentHelper.isAfter(f.getScheduledDeparture(), currentMoment))).toList();

		flightChoices = SelectChoices.from(flights, "tag", booking.getFlight());
		travelChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "lastCardNibble", "price", "purchasedAt", "draftMode");
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("travelClass", travelChoices.getSelected().getKey());
		dataset.put("travelClasses", travelChoices);

		super.getResponse().addData(dataset);
	}
}
