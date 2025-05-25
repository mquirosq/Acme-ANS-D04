
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
		int taskRecordId;
		TaskRecord taskRecord;
		Technician technician;
		boolean status;
		String taskRecordIdString;

		try {
			taskRecordIdString = super.getRequest().getData("id", String.class);
			taskRecordId = Integer.parseInt(taskRecordIdString);
			taskRecord = this.repository.findTaskRecordById(taskRecordId);
			technician = taskRecord == null ? null : taskRecord.getRecord().getTechnician();
			if (taskRecord == null)
				status = false;
			else if (!super.getRequest().getPrincipal().hasRealm(technician))
				status = false;
			else
				status = true;
		} catch (NumberFormatException | AssertionError e) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;
		TaskRecord taskRecord;

		id = super.getRequest().getData("id", int.class);
		taskRecord = this.repository.findTaskRecordById(id);

		maintenanceRecord = taskRecord.getRecord();
		taskRecord.setRecord(maintenanceRecord);

		super.getBuffer().addData(taskRecord);
	}

	@Override
	public void unbind(final TaskRecord taskRecord) {
		Collection<Task> tasks;
		SelectChoices taskChoices;
		SelectChoices technicianChoices;
		Dataset dataset;

		int id = super.getRequest().getData("id", int.class);
		tasks = this.repository.findAllTasksByTaskRecordId(id);
		taskChoices = SelectChoices.from(tasks, "type", taskRecord.getTask());
		int priority = taskRecord.getTask().getPriority();
		technicianChoices = SelectChoices.from(tasks, "technician.identity.fullName", taskRecord.getTask());
		int estimate = taskRecord.getTask().getHourEstimate();
		String description = taskRecord.getTask().getDescription();

		dataset = super.unbindObject(taskRecord);
		dataset.put("task", taskRecord.getTask().getId());
		dataset.put("types", taskChoices);
		dataset.put("type", taskChoices.getSelected().getKey());
		dataset.put("priority", priority);
		dataset.put("technicians", technicianChoices);
		dataset.put("technician", technicianChoices.getSelected().getKey());
		dataset.put("estimate", estimate);
		dataset.put("description", description);

		super.getResponse().addData(dataset);

	}
}
