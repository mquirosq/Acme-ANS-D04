
package acme.features.any.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Service;

@GuiService
public class AnyServiceListService extends AbstractGuiService<Any, Service> {

	@Autowired
	private AnyServiceRepository repository;


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
