
package acme.features.administrator.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.AdministratorDashboard;

@GuiController
public class AdministratorDashboardController extends AbstractGuiController<Administrator, AdministratorDashboard> {

	private final AdministratorDashboardShowService showService;


	@Autowired
	public AdministratorDashboardController(final AdministratorDashboardShowService showService) {
		this.showService = showService;
	}

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
