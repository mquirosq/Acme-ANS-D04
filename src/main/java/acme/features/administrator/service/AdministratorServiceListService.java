
package acme.features.administrator.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Service;

@GuiService
public class AdministratorServiceListService extends AbstractGuiService<Administrator, Service> {

	private final AdministratorServiceRepository repository;


	@Autowired
	public AdministratorServiceListService(final AdministratorServiceRepository repository) {
		this.repository = repository;
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Service> services;

		services = this.repository.findAllServices();

		super.getBuffer().addData(services);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "avgDwellTime");
		super.addPayload(dataset, service, "promotionCode", "promotionDiscount");

		super.getResponse().addData(dataset);
	}

}
