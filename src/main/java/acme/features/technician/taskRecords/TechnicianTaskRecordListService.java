
package acme.features.technician.taskRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.MaintenanceRecord;
import acme.entities.TaskRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskRecordListService extends AbstractGuiService<Technician, TaskRecord> {

	@Autowired
	private TechnicianTaskRecordRepository repository;


	@Override
	public void authorise() {
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		boolean status;
		String maintenanceRecordIdString;
		Technician technician;

		try {
			maintenanceRecordIdString = super.getRequest().getData("id", String.class);
			maintenanceRecordId = Integer.parseInt(maintenanceRecordIdString);
			maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
			technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();

			if (maintenanceRecord == null)
				status = false;
			else
				status = !maintenanceRecord.isDraftMode() || super.getRequest().getPrincipal().hasRealm(technician);
		} catch (NumberFormatException | AssertionError e) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int maintenanceRecordId;
		Collection<TaskRecord> taskRecords;

		maintenanceRecordId = super.getRequest().getData("id", int.class);
		taskRecords = this.repository.findTaskRecordsByMaintenanceRecordId(maintenanceRecordId);

		super.getBuffer().addData(taskRecords);
		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
	}

	@Override
	public void unbind(final TaskRecord taskRecord) {
		Dataset dataset;

		dataset = super.unbindObject(taskRecord);
		dataset.put("task.type", taskRecord.getTask().getType());
		dataset.put("task.technician", taskRecord.getTask().getTechnician().getIdentity().getFullName());
		dataset.put("task.priority", taskRecord.getTask().getPriority());
		super.addPayload(dataset, taskRecord);
		super.getResponse().addData(dataset);
	}
}
