
package acme.features.administrator.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Booking;

@GuiController
public class AdministratorBookingController extends AbstractGuiController<Administrator, Booking> {

	private final AdministratorBookingListService	listService;
	private final AdministratorBookingShowService	showService;


	@Autowired
	public AdministratorBookingController(final AdministratorBookingListService listService, final AdministratorBookingShowService showService) {
		this.listService = listService;
		this.showService = showService;
	}

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
