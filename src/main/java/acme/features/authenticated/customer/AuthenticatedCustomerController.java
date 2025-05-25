
package acme.features.authenticated.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.Customer;

@GuiController
public class AuthenticatedCustomerController extends AbstractGuiController<Authenticated, Customer> {

	// Internal state ---------------------------------------------------------

	private final AuthenticatedCustomerCreateService	createService;

	private final AuthenticatedCustomerUpdateService	updateService;


	@Autowired
	public AuthenticatedCustomerController(final AuthenticatedCustomerCreateService createService, final AuthenticatedCustomerUpdateService updateService) {
		this.createService = createService;
		this.updateService = updateService;
	}

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}

}
