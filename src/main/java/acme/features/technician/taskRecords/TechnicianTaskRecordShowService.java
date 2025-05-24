
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
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		boolean status;
		int technicianId;
		int taskId;
		Task task;
		boolean taskStatus;
		boolean alreadyAddedToTheRecord;
		Task alreadyAddedTask;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		if (maintenanceRecord == null)
			status = false;
		else {
			if (super.getRequest().getMethod().equals("GET"))
				taskStatus = true;
			else {
				technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

				taskId = super.getRequest().getData("task", int.class);
				task = this.repository.findTechniciansTaskByIds(taskId, technicianId);
				alreadyAddedTask = this.repository.findValidTaskByIdAndMaintenanceRecord(taskId, maintenanceRecordId);
				alreadyAddedToTheRecord = alreadyAddedTask != null;
				taskStatus = taskId == 0 || task != null && !alreadyAddedToTheRecord;
			}
			status = maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician()) && taskStatus;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;
		TaskRecord taskRecord;

		id = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		taskRecord = new TaskRecord();
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
