
package acme.features.technician.tasks;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TaskType;
import acme.entities.Task;
import acme.entities.TaskRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Technician technician;
		Task task;
		String masterIdString;

		try {
			masterIdString = super.getRequest().getData("id", String.class);
			masterId = Integer.parseInt(masterIdString);
			task = this.repository.findTaskById(masterId);
			technician = task == null ? null : task.getTechnician();
			if (task == null)
				status = false;
			else if (!task.getIsDraft() || !super.getRequest().getPrincipal().hasRealm(technician))
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
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(task, "type", "description", "priority", "hourEstimate");
		task.setTechnician(technician);
	}

	@Override
	public void validate(final Task task) {

	}

	@Override
	public void perform(final Task task) {
		Collection<TaskRecord> taskRecords;

		taskRecords = this.repository.getTaskRecordsByTaskId(task.getId());

		this.repository.deleteAll(taskRecords);
		this.repository.delete(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "description", "priority", "hourEstimate", "isDraft");
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
