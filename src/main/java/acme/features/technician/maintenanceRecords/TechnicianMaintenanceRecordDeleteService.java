
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		String method;

		method = super.getRequest().getMethod();

		id = super.getRequest().getData("id", int.class);
		mRecord = this.repository.findMaintenanceRecordbyId(id);

		if (mRecord == null)
			status = false;
		else
			status = mRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(mRecord.getTechnician()) && !method.equals("GET");

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

}
