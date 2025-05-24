
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
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean tasksPublished, acStatus;
		int mRecordId, aircraftId;
		MaintenanceRecord mRecord;
		Technician technician;
		Collection<Task> tasks;
		Aircraft aircraft;
		String method;

		method = super.getRequest().getMethod();

		mRecordId = super.getRequest().getData("id", int.class);
		mRecord = this.repository.findMaintenanceRecordbyId(mRecordId);
		technician = mRecord == null ? null : mRecord.getTechnician();
		tasks = this.repository.findTasksByMaintenanceRecordId(mRecordId);

		status = mRecord != null && mRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);
		tasksPublished = !tasks.isEmpty() && tasks.stream().allMatch(t -> !t.getIsDraft());

		if (mRecord == null)
			status = false;
		else if (!mRecord.isDraftMode() || !super.getRequest().getPrincipal().hasRealm(technician))
			status = false;
		else if (super.getRequest().getMethod().equals("GET"))
			status = tasksPublished;
		else {
			aircraftId = super.getRequest().getData("aircraft", int.class);
			aircraft = this.repository.findAircraftById(aircraftId);
			acStatus = aircraftId == 0 || aircraft != null;

			status = acStatus;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord mRecord;
		int mRecordId;

		mRecordId = super.getRequest().getData("id", int.class);
		mRecord = this.repository.findMaintenanceRecordbyId(mRecordId);

		super.getBuffer().addData(mRecord);
	}

	@Override
	public void bind(final MaintenanceRecord mRecord) {
		Technician technician;
		Aircraft aircraft;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		aircraft = this.repository.findAircraftById(super.getRequest().getData("aircraft", int.class));

		super.bindObject(mRecord, "status", "maintenanceDate", "inspectionDue", "cost", "notes", "aircraft");
		mRecord.setTechnician(technician);
		mRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord mRecord) {

	}

	@Override
	public void perform(final MaintenanceRecord mRecord) {
		mRecord.setStatus(recordStatus.COMPLETED);
		this.repository.save(mRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord mRecord) {
		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;
		boolean publish;
		Collection<Task> tasks;

		tasks = this.repository.findTasksByMaintenanceRecordId(mRecord.getId());
		publish = !tasks.isEmpty() && tasks.stream().allMatch(t -> !t.getIsDraft());

		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", mRecord.getAircraft());

		statusChoices = SelectChoices.from(recordStatus.class, mRecord.getStatus());

		dataset = super.unbindObject(mRecord, "maintenanceDate", "inspectionDue", "cost", "notes");
		dataset.put("id", mRecord.getId());
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("publish", publish);

		super.getResponse().addData(dataset);
	}
}
