
package acme.features.administrator.bannedPassenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.BannedPassenger;

@GuiService
public class AdministratorBannedPassengerShowService extends AbstractGuiService<Administrator, BannedPassenger> {

	@Autowired
	private AdministratorBannedPassengerRepository repository;


	@Override
	public void authorise() {
		Boolean authorised;
		String rawId;
		int bannedPassengerId;
		BannedPassenger bannedPassenger;

		try {
			rawId = super.getRequest().getData("id", String.class);
			bannedPassengerId = Integer.parseInt(rawId);
			bannedPassenger = this.repository.findBannedPassengerById(bannedPassengerId);
			authorised = bannedPassenger != null;
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id;
		BannedPassenger bannedPassenger;

		id = super.getRequest().getData("id", int.class);
		bannedPassenger = this.repository.findBannedPassengerById(id);

		super.getBuffer().addData(bannedPassenger);
	}

	@Override
	public void unbind(final BannedPassenger bannedPassenger) {
		Dataset dataset;

		dataset = super.unbindObject(bannedPassenger, "fullName", "passportNumber", "issuedAt", "liftedAt", "nationality", "dateOfBirth", "reason");

		super.getResponse().addData(dataset);
	}
}
