
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircraft;
import acme.entities.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> records;
		Collection<MaintenanceRecord> publishedRecords;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		records = this.repository.findMaintenanceRecordsByTechnicianId(technicianId);
		publishedRecords = this.repository.findPublishedMaintenanceRecords();
		records.addAll(publishedRecords);
		super.getBuffer().addData(records);
	}

	@Override
	public void unbind(final MaintenanceRecord mRecord) {
		Aircraft aircraft;
		Dataset dataset;
		Boolean isDraft;

		aircraft = this.repository.findAircraftByRecordId(mRecord.getId());
		isDraft = mRecord.isDraftMode();

		dataset = super.unbindObject(mRecord, "inspectionDue", "maintenanceDate");
		dataset.put("aircraft", aircraft.getRegistrationNumber());
		dataset.put("draftMode", isDraft);
		super.addPayload(dataset, mRecord, "cost");

		super.getResponse().addData(dataset);
	}

}
