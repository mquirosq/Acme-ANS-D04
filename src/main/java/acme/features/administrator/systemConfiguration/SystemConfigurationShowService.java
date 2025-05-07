
package acme.features.administrator.systemConfiguration;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.SystemConfiguration;

@GuiService
public class SystemConfigurationShowService extends AbstractGuiService<Administrator, SystemConfiguration> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SystemConfigurationRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SystemConfiguration config;

		config = this.repository.getSystemConfig();

		super.getBuffer().addData(config);
	}

	@Override
	public void unbind(final SystemConfiguration config) {
		Dataset dataset;

		dataset = super.unbindObject(config, "systemCurrency", "acceptedCurrencies");

		super.getResponse().addData(dataset);
	}

}
