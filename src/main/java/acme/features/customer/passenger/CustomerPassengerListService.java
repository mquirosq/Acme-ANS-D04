
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Passenger;
import acme.helpers.InternationalisationHelper;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Boolean authorised;
		String rawBoolean;

		try {
			rawBoolean = super.getRequest().getData("all", String.class);
			authorised = rawBoolean.equals("true") || rawBoolean.equals("false");
		} catch (AssertionError e) {
			authorised = false;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean allMode = super.getRequest().getData("all", boolean.class);
		if (allMode)
			passengers = this.repository.findAllPassengersOfCustomer(customerId);
		else
			passengers = this.repository.findPassengersOfCustomer(customerId);

		super.getBuffer().addData(passengers);
		super.getResponse().addGlobal("all", allMode);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "passportNumber");
		super.addPayload(dataset, passenger, "email", "birthDate");
		dataset.put("payload", dataset.get("payload") + "|" + InternationalisationHelper.internationalizeBoolean(passenger.isDraftMode()));

		super.getResponse().addData(dataset);
	}
}
