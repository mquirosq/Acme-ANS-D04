
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
import acme.components.MoneyExchangeHelper;
import acme.datatypes.TravelClass;
import acme.entities.Booking;
import acme.entities.Flight;
import acme.forms.MoneyExchange;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	private final CustomerBookingRepository repository;


	@Autowired
	public CustomerBookingUpdateService(final CustomerBookingRepository repository) {
		this.repository = repository;
	}

	// AbstractService<Customer, Booking> -------------------------------------

	@Override
	public void authorise() {
		boolean authorised;
		String rawId;
		int bookingId;
		Booking booking;

		try {
			rawId = super.getRequest().getData("id", String.class);
			bookingId = Integer.parseInt(rawId);
			booking = this.repository.findBookingById(bookingId);

			authorised = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && MomentHelper.isAfter(booking.getFlight().getScheduledDeparture(), MomentHelper.getCurrentMoment());
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}

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
				authorised &= flight != null && !flight.getDraftMode() && MomentHelper.isAfter(flight.getScheduledDeparture(), MomentHelper.getCurrentMoment());
			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Booking booking;
		int bookingId;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "travelClass", "lastCardNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		// Intentionally left empty: no extra validation needed for Booking in this context.
	}

	@Override
	public void perform(final Booking booking) {
		Money price;
		Long numberPassengers;
		Date currentMoment;

		currentMoment = MomentHelper.getCurrentMoment();
		numberPassengers = this.repository.countPassengersInBooking(booking.getId());

		price = new Money();

		price.setAmount(booking.getFlight().getCost().getAmount() * numberPassengers);
		price.setCurrency(booking.getFlight().getCost().getCurrency());

		booking.setPrice(price);
		booking.setPurchasedAt(currentMoment);
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
		flights = flights.stream().filter(f -> MomentHelper.isAfter(f.getScheduledDeparture(), currentMoment)).toList();

		flightChoices = SelectChoices.from(flights, "identifierCode", booking.getFlight());
		travelChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "lastCardNibble", "price", "purchasedAt", "draftMode");
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("travelClass", travelChoices.getSelected().getKey());
		dataset.put("travelClasses", travelChoices);
		dataset.put("readonly", false);

		String systemCurrency = this.repository.getSystemCurrency();

		Money exchangedPrice;
		if (systemCurrency.equals(booking.getPrice().getCurrency()))
			exchangedPrice = null;
		else {
			MoneyExchange exchange = new MoneyExchange();
			exchange.setSource(booking.getPrice());
			exchange.setTargetCurrency(systemCurrency);
			exchange = MoneyExchangeHelper.performExchangeToSystemCurrency(exchange);
			exchangedPrice = exchange.getTarget();
			super.state(exchange.getOops() == null, "*", exchange.getMessage());
		}
		dataset.put("systemPrice", exchangedPrice);

		super.getResponse().addData(dataset);
	}
}
