
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		Dataset dataset;

		dataset = super.unbindObject(record, "aircraft", "status", "inspectionDue", "notes");
		super.addPayload(dataset, record, "maintenanceDate", "cost");

		super.getResponse().addData(dataset);
	}

}
