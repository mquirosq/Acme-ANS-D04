
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean authorised;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);

		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);

		authorised = passenger != null && passenger.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds", "draftMode");
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds", "draftMode");
		dataset.put("readonly", false);
		dataset.put("draftMode", true);

		super.getResponse().addData(dataset);
	}
}
