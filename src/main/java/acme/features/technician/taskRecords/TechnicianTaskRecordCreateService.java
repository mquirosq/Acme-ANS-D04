
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
		TaskRecord taskRecord;

		taskRecord = new TaskRecord();

		int maintenanceRecordId = super.getRequest().getData("id", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		taskRecord.setRecord(maintenanceRecord);

		super.getBuffer().addData(taskRecord);
		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
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
		Collection<Task> tasks;
		SelectChoices choices;
		Dataset dataset;

		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int maintenanceRecordId = taskRecord.getRecord().getId();
		tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecordId);
		choices = SelectChoices.from(tasks, "type", taskRecord.getTask());

		dataset = super.unbindObject(taskRecord);
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);

		super.getResponse().addData(dataset);
	}

}
