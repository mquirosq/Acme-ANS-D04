
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.recordStatus;
import acme.entities.Aircraft;
import acme.entities.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecord mRecord;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		mRecord = new MaintenanceRecord();
		mRecord.setStatus(recordStatus.PENDING);
		mRecord.setTechnician(technician);

		super.getBuffer().addData(mRecord);
	}

	@Override
	public void bind(final MaintenanceRecord mRecord) {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(mRecord, "status", "inspectionDue", "maintenanceDate", "cost", "notes", "aircraft");
		mRecord.setTechnician(technician);
	}

	@Override
	public void validate(final MaintenanceRecord mRecord) {

	}

	@Override
	public void perform(final MaintenanceRecord mRecord) {
		this.repository.save(mRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord mRecord) {
		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;

		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", mRecord.getAircraft());

		statusChoices = SelectChoices.from(recordStatus.class, mRecord.getStatus());

		dataset = super.unbindObject(mRecord, "maintenanceDate", "inspectionDue", "cost", "notes");
		dataset.put("id", mRecord.getId());
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);

		super.getResponse().addData(dataset);
	}
}
