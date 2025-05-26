
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Booking;
import acme.helpers.InternationalisationHelper;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	private final CustomerBookingRepository repository;


	@Autowired
	public CustomerBookingListService(final CustomerBookingRepository repository) {
		this.repository = repository;
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		bookings = this.repository.findBookingsByCustomerId(customerId);

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		String flight;

		dataset = super.unbindObject(booking, "locatorCode", "purchasedAt", "price");
		dataset.put("draftMode", InternationalisationHelper.internationalizeBoolean(booking.isDraftMode()));

		flight = booking.getFlight().getIdentifierCode();
		dataset.put("flight", flight);

		super.addPayload(dataset, booking, "travelClass", "lastCardNibble", "purchasedAt");

		super.getResponse().addData(dataset);
	}
}
