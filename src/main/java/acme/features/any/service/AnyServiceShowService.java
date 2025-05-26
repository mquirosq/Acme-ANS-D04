
package acme.features.any.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Service;

@GuiService
public class AnyServiceShowService extends AbstractGuiService<Any, Service> {

	private final AnyServiceRepository repository;


	@Autowired
	public AnyServiceShowService(final AnyServiceRepository repository) {
		this.repository = repository;
	}

	@Override
	public void authorise() {
		Boolean authorised;
		String rawId;
		int serviceId;
		Service service;

		try {
			rawId = super.getRequest().getData("id", String.class);
			serviceId = Integer.parseInt(rawId);
			service = this.repository.findServiceById(serviceId);
			authorised = service != null;
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Service service;
		int id;

		id = super.getRequest().getData("id", int.class);
		service = this.repository.findServiceById(id);

		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "pictureLink", "avgDwellTime", "promotionCode", "promotionDiscount");
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
