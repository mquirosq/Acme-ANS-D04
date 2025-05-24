
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
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean acStatus;
		int mRecordId;
		int aircraftId;
		MaintenanceRecord mRecord;
		Technician technician;
		Aircraft aircraft;
		String mRecordIdString;
		String aircraftIdString;

		try {
			mRecordIdString = super.getRequest().getData("id", String.class);
			mRecordId = Integer.parseInt(mRecordIdString);
			mRecord = this.repository.findMaintenanceRecordbyId(mRecordId);
			technician = mRecord == null ? null : mRecord.getTechnician();
			if (mRecord == null)
				status = false;
			else if (!mRecord.isDraftMode() || !super.getRequest().getPrincipal().hasRealm(technician))
				status = false;
			else if (super.getRequest().getMethod().equals("GET"))
				status = true;
			else {
				aircraftIdString = super.getRequest().getData("aircraft", String.class);
				aircraftId = Integer.parseInt(aircraftIdString);
				aircraft = this.repository.findAircraftById(aircraftId);
				acStatus = aircraftId == 0 || aircraft != null;
				status = acStatus;
			}
		} catch (NumberFormatException | AssertionError e) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord mRecord;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		mRecord = new MaintenanceRecord();
		mRecord.setStatus(recordStatus.PENDING);
		mRecord.setTechnician(technician);

		super.getBuffer().addData(mRecord);
	}

	@Override
	public void bind(final MaintenanceRecord mRecord) {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		Aircraft aircraft = this.repository.findAircraftById(super.getRequest().getData("aircraft", int.class));

		super.bindObject(mRecord, "inspectionDue", "maintenanceDate", "cost", "notes");
		mRecord.setTechnician(technician);
		mRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord mRecord) {

	}

	@Override
	public void perform(final MaintenanceRecord mRecord) {
		mRecord.setStatus(recordStatus.PENDING);
		this.repository.save(mRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord mRecord) {
		Dataset dataset;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;
		boolean publishable;
		Collection<Task> tasks;

		tasks = this.repository.findTasksByMaintenanceRecordId(mRecord.getId());
		publishable = mRecord.isDraftMode() && !tasks.isEmpty() && tasks.stream().allMatch(t -> !t.getIsDraft());

		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", mRecord.getAircraft());
		boolean draftMode = mRecord.isDraftMode();

		dataset = super.unbindObject(mRecord, "maintenanceDate", "inspectionDue", "cost", "notes");
		dataset.put("id", mRecord.getId());
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("draftMode", draftMode);
		dataset.put("publishable", publishable);

		super.getResponse().addData(dataset);
	}
}
