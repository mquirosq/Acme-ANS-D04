
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
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		records = this.repository.findMaintenanceRecordsByTechnicianId(technicianId);

		super.getBuffer().addData(records);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Aircraft aircraft;
		Dataset dataset;
		Boolean isDraft;

		aircraft = this.repository.findAircraftByRecordId(record.getId());
		isDraft = record.isDraftMode();

		dataset = super.unbindObject(record, "inspectionDue", "maintenanceDate");
		dataset.put("aircraft", aircraft.getModel());
		dataset.put("draftMode", isDraft);
		super.addPayload(dataset, record, "cost");

		super.getResponse().addData(dataset);
	}

}
