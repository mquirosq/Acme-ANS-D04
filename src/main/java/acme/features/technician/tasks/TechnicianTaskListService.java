
package acme.features.technician.tasks;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Task> tasks;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		tasks = this.repository.findAllTasksByTechnicianId(technicianId);

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "priority", "hourEstimate");
		super.addPayload(dataset, task, "isDraft");

		super.getResponse().addData(dataset);
	}
}
