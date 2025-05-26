
package acme.features.administrator.bannedPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.BannedPassenger;

@GuiService
public class AdministratorBannedPassengerListCurrentService extends AbstractGuiService<Administrator, BannedPassenger> {

	@Autowired
	private AdministratorBannedPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<BannedPassenger> currentlyBannedPassengers;

		currentlyBannedPassengers = this.repository.findCurrentlyBannedPassengers();

		super.getBuffer().addData(currentlyBannedPassengers);
	}

	@Override
	public void unbind(final BannedPassenger bannedPassenger) {
		Dataset dataset;

		dataset = super.unbindObject(bannedPassenger, "fullName", "passportNumber", "issuedAt", "liftedAt");
		super.addPayload(dataset, bannedPassenger, "nationality", "dateOfBirth", "reason");

		super.getResponse().addData(dataset);
	}
}
