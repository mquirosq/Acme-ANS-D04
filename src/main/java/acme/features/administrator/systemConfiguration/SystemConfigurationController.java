
package acme.features.administrator.systemConfiguration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.SystemConfiguration;

@GuiController
public class SystemConfigurationController extends AbstractGuiController<Administrator, SystemConfiguration> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SystemConfigurationShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
