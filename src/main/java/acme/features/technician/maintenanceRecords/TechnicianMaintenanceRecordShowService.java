
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.MoneyExchangeHelper;
import acme.datatypes.recordStatus;
import acme.entities.Aircraft;
import acme.entities.MaintenanceRecord;
import acme.forms.MoneyExchange;
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
		status = record != null && (!record.isDraftMode() || super.getRequest().getPrincipal().getActiveRealm().getId() == technician.getId());

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
		Collection<Aircraft> aircrafts;
		SelectChoices statusChoices, aircraftChoices;
		Dataset dataset;

		aircrafts = this.repository.findAllAircrafts();
		statusChoices = SelectChoices.from(recordStatus.class, record.getStatus());
		aircraftChoices = SelectChoices.from(aircrafts, "model", record.getAircraft());
		boolean draftMode = record.isDraftMode();

		dataset = super.unbindObject(record, "maintenanceDate", "inspectionDue", "cost", "notes");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("draftMode", draftMode);

		String systemCurrency = this.repository.getSystemCurrency();

		Money exchangedPrice;
		if (systemCurrency.equals(record.getCost().getCurrency()))
			exchangedPrice = null;
		else {
			MoneyExchange exchange = new MoneyExchange();
			exchange.setSource(record.getCost());
			exchange.setTargetCurrency(systemCurrency);
			exchange = MoneyExchangeHelper.performExchangeToSystemCurrency(exchange);
			exchangedPrice = exchange.getTarget();
			super.state(exchange.getOops() == null, "*", exchange.getMessage());
		}
		dataset.put("systemPrice", exchangedPrice);

		super.getResponse().addData(dataset);

	}
}
