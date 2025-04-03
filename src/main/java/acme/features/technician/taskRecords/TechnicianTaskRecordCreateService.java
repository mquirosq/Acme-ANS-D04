
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
public class TechnicianTaskRecordCreateService extends AbstractGuiService<Technician, TaskRecord> {

	@Autowired
	private TechnicianTaskRecordRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int maintenanceRecordId = super.getRequest().getData("id", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		Technician technician = maintenanceRecord.getTechnician();

		authorised = maintenanceRecord != null && super.getRequest().getPrincipal().hasRealm(technician);
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;
		TaskRecord taskRecord;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		taskRecord = new TaskRecord();
		taskRecord.setRecord(maintenanceRecord);

		super.getBuffer().addData(taskRecord);
	}

	@Override
	public void bind(final TaskRecord taskRecord) {
		super.bindObject(taskRecord, "task");
	}

	@Override
	public void validate(final TaskRecord taskRecord) {

	}

	@Override
	public void perform(final TaskRecord taskRecord) {
		this.repository.save(taskRecord);
	}

	@Override
	public void unbind(final TaskRecord taskRecord) {
		Dataset dataset;
		SelectChoices taskChoices;
		Collection<Task> tasks;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("id", int.class);

		tasks = this.repository.findNewTasksforMaintenanceRecord(maintenanceRecordId);
		taskChoices = SelectChoices.from(tasks, "description", taskRecord.getTask());

		dataset = super.unbindObject(taskRecord);
		dataset.put("tasks", taskChoices);
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("id", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}

}
