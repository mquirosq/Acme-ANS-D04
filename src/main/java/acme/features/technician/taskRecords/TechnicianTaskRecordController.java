
package acme.features.technician.taskRecords;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.TaskRecord;
import acme.realms.Technician;

@GuiController
public class TechnicianTaskRecordController extends AbstractGuiController<Technician, TaskRecord> {

	@Autowired
	private TechnicianTaskRecordListService		listService;

	@Autowired
	private TechnicianTaskRecordShowService		showService;

	@Autowired
	private TechnicianTaskRecordCreateService	createService;

	@Autowired
	private TechnicianTaskRecordDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
