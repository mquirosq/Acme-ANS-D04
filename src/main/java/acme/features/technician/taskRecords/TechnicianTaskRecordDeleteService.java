
package acme.features.technician.taskRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Task;
import acme.entities.TaskRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskRecordDeleteService extends AbstractGuiService<Technician, TaskRecord> {

	@Autowired
	private TechnicianTaskRecordRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int taskRecordId = super.getRequest().getData("id", int.class);
		TaskRecord taskRecord = this.repository.findTaskRecordById(taskRecordId);

		Technician technician = taskRecord.getRecord().getTechnician();

		authorised = taskRecord != null && super.getRequest().getPrincipal().hasRealm(technician);
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		TaskRecord taskRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		taskRecord = this.repository.findTaskRecordById(id);

		super.getBuffer().addData(taskRecord);
	}

	@Override
	public void bind(final TaskRecord taskRecord) {
		super.bindObject(taskRecord);
	}

	@Override
	public void validate(final TaskRecord taskRecord) {

	}

	@Override
	public void perform(final TaskRecord taskRecord) {
		this.repository.delete(taskRecord);
	}

	@Override
	public void unbind(final TaskRecord taskRecord) {
		Collection<Task> tasks;
		SelectChoices taskChoices, technicianChoices;
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
