
package acme.features.technician.taskRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.MaintenanceRecord;
import acme.entities.Task;
import acme.entities.TaskRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskRecordShowService extends AbstractGuiService<Technician, TaskRecord> {

	@Autowired
	private TechnicianTaskRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord record;
		id = super.getRequest().getData("id", int.class);
		record = this.repository.findMaintenanceRecordOfTaskRecordById(id);
		status = record != null && super.getRequest().getPrincipal().hasRealm(record.getTechnician());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TaskRecord taskRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		taskRecord = this.repository.findTaskRecordById(id);
		super.getBuffer().addData(taskRecord);
		super.getResponse().addGlobal("maintenanceRecordId", taskRecord.getRecord().getId());
	}

	@Override
	public void unbind(final TaskRecord taskRecord) {
		Collection<Task> tasks;
		SelectChoices choices;
		Dataset dataset;

		int id = super.getRequest().getData("id", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordOfTaskRecordById(id);
		tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecord.getId());
		choices = SelectChoices.from(tasks, "type", taskRecord.getTask());

		dataset = super.unbindObject(taskRecord);
		dataset.put("task", taskRecord.getTask().getId());
		dataset.put("tasks", choices);

		super.getResponse().addData(dataset);

	}
}
