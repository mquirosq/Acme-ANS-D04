
package acme.features.administrator.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Service;

@GuiService
public class AdministratorServiceCreateService extends AbstractGuiService<Administrator, Service> {

	private final AdministratorServiceRepository repository;


	@Autowired
	public AdministratorServiceCreateService(final AdministratorServiceRepository repository) {
		this.repository = repository;
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Service service;

		service = new Service();

		super.getBuffer().addData(service);
	}

	@Override
	public void bind(final Service service) {
		super.bindObject(service, "name", "pictureLink", "avgDwellTime", "promotionCode", "promotionDiscount");
	}

	@Override
	public void validate(final Service service) {
		// Intentionally left empty: no extra validation needed for Service in this context.
	}

	@Override
	public void perform(final Service service) {
		this.repository.save(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "pictureLink", "avgDwellTime", "promotionCode", "promotionDiscount");
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
