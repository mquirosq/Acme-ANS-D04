
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractService<Customer, Passenger> -------------------------------------


	@Override
	public void authorise() {

		boolean authorised;
		String rawId;
		int passengerId;
		Passenger passenger;

		try {
			rawId = super.getRequest().getData("id", String.class);
			passengerId = Integer.parseInt(rawId);
			passenger = this.repository.findPassengerById(passengerId);

			authorised = passenger != null && passenger.isDraftMode() && super.getRequest().getPrincipal().getActiveRealm().equals(passenger.getCustomer());
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}

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
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void perform(final Passenger passenger) {
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
