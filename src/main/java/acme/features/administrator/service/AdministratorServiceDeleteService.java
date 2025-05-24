
package acme.features.administrator.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Service;

@GuiService
public class AdministratorServiceDeleteService extends AbstractGuiService<Administrator, Service> {

	@Autowired
	private AdministratorServiceRepository repository;


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
		} catch (NumberFormatException e) {
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
	public void bind(final Service service) {
		;
	}

	@Override
	public void validate(final Service service) {
		;
	}

	@Override
	public void perform(final Service service) {
		this.repository.delete(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "pictureLink", "avgDwellTime", "promotionCode", "promotionDiscount");
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
