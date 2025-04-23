
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
import acme.entities.TaskRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord mRecord;
		Technician technician;

		id = super.getRequest().getData("id", int.class);
		mRecord = this.repository.findMaintenanceRecordbyId(id);
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		status = mRecord != null && mRecord.isDraftMode() && mRecord.getTechnician().getId() == technician.getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord mRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		mRecord = this.repository.findMaintenanceRecordbyId(id);

		super.getBuffer().addData(mRecord);
	}

	@Override
	public void bind(final MaintenanceRecord mRecord) {
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(mRecord, "status", "maintenanceDate", "inspectionDue", "cost", "notes", "aircraft");
		mRecord.setTechnician(technician);
	}

	@Override
	public void validate(final MaintenanceRecord mRecord) {

	}

	@Override
	public void perform(final MaintenanceRecord mRecord) {
		Collection<TaskRecord> taskRecords;

		taskRecords = this.repository.getTaskRecordsByMaintenanceRecordId(mRecord.getId());

		this.repository.deleteAll(taskRecords);
		this.repository.delete(mRecord);
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
