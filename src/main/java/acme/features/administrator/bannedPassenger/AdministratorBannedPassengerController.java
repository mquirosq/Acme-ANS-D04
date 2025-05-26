
package acme.features.administrator.bannedPassenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.BannedPassenger;

@GuiController
public class AdministratorBannedPassengerController extends AbstractGuiController<Administrator, BannedPassenger> {

	@Autowired
	private AdministratorBannedPassengerListCurrentService	listCurrentService;

	@Autowired
	private AdministratorBannedPassengerShowService			showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("list-current", "list", this.listCurrentService);
	}
}
