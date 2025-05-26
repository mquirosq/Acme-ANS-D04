
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.ActivityLog;
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegDeleteService extends AbstractGuiService<AirlineManager, FlightLeg> {

	@Autowired
	private AirlineManagerFlightLegRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		String legIdInput;
		int legId, managerId;
		FlightLeg leg;
		AirlineManager manager;

		try {
			legIdInput = super.getRequest().getData("id", String.class);
			legId = Integer.parseInt(legIdInput);
			leg = this.repository.findFlightLegById(legId);
			managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			manager = this.repository.findManagerById(managerId);
			authorised = leg != null && leg.getDraftMode() && leg.getParentFlight() != null && leg.getParentFlight().getManager() != null && leg.getParentFlight().getManager().equals(manager);
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		FlightLeg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findFlightLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final FlightLeg leg) {
	}

	@Override
	public void validate(final FlightLeg leg) {
	}

	@Override
	public void perform(final FlightLeg leg) {
		Collection<FlightAssignment> assignments;
		Collection<ActivityLog> logs;

		assignments = this.repository.findAllAssignmentsByFlightLegId(leg.getId());
		logs = this.repository.findAllActivityLogsByFlightLegId(leg.getId());

		this.repository.deleteAll(logs);
		this.repository.deleteAll(assignments);
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final FlightLeg leg) {

	}

}
