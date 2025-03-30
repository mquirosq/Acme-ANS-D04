
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


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
		String draftMode = "";

		dataset = super.unbindObject(booking, "locatorCode", "purchasedAt", "price", "flight");

		if (booking.isDraftMode())
			draftMode = "Pending";
		else
			draftMode = "Complete";
		dataset.put("draftMode", draftMode);

		super.addPayload(dataset, booking, //
			"travelClass", "lastCardNibble");

		super.getResponse().addData(dataset);
	}
}
