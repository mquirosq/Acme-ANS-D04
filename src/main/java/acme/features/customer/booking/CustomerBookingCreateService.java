
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
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractService<Customer, Booking> -------------------------------------


	@Override
	public void authorise() {
		Boolean authorised = true;

		String flightIdRaw;
		int flightId;
		Flight flight;

		if (super.getRequest().hasData("flight")) {
			flightIdRaw = super.getRequest().getData("flight", String.class);

			try {
				flightId = Integer.parseInt(flightIdRaw);
			} catch (NumberFormatException e) {
				flightId = -1;
				authorised = false;
			}

			if (flightId != 0) {
				flight = this.repository.findFlightById(flightId);
				authorised &= flight != null && !flight.getDraftMode() && flight.getScheduledDeparture() != null && MomentHelper.isAfter(flight.getScheduledDeparture(), MomentHelper.getCurrentMoment());
			}
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Booking booking;

		booking = new Booking();

		Date currentMoment = MomentHelper.getCurrentMoment();
		Money price = new Money();

		price.setAmount(0.0);
		price.setCurrency("EUR");

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		booking.setCustomer(customer);
		booking.setDraftMode(true);
		booking.setPrice(price);
		booking.setPurchasedAt(currentMoment);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchasedAt", "travelClass", "lastCardNibble", "flight");
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

		dataset = super.unbindObject(booking, "locatorCode", "lastCardNibble", "price", "purchasedAt");
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("travelClass", travelChoices.getSelected().getKey());
		dataset.put("travelClasses", travelChoices);

		super.getResponse().addData(dataset);
	}
}
