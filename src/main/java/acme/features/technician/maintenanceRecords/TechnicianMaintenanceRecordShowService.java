
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.recordStatus;
import acme.entities.Aircraft;
import acme.entities.MaintenanceRecord;
import acme.entities.Task;
import acme.entities.TaskRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordShowService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Technician technician;
		MaintenanceRecord record;

		masterId = super.getRequest().getData("id", int.class);
		record = this.repository.findMaintenanceRecordbyId(masterId);
		technician = record == null ? null : record.getTechnician();
		status = super.getRequest().getPrincipal().hasRealm(technician) || record != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord record;
		int id;

		id = super.getRequest().getData("id", int.class);
		record = this.repository.findMaintenanceRecordbyId(id);

		super.getBuffer().addData(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Collection<Task> tasks;
		Collection<Aircraft> aircrafts;
		Collection<TaskRecord> recordTask;
		SelectChoices taskChoices, statusChoices, aircraftChoices;
		Dataset dataset;

		recordTask = this.repository.getTaskRecordsByMaintenanceRecordId(record.getId());
		tasks = this.repository.findTasksByMaintenanceRecordId(record.getId());
		aircrafts = this.repository.findAllAircrafts();
		//taskChoices = SelectChoices.from(tasks, "type", null);
		statusChoices = SelectChoices.from(recordStatus.class, record.getStatus());
		aircraftChoices = SelectChoices.from(aircrafts, "model", record.getAircraft());

		dataset = super.unbindObject(record, "maintenanceDate", "inspectionDue", "cost", "notes");
		//dataset.put("task", taskChoices.getSelected().getKey());
		//dataset.put("tasks", taskChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());

		super.getResponse().addData(dataset);

	}
}
