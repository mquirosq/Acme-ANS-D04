
package acme.features.airlineManager.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Flight;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerFlightController extends AbstractGuiController<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightListService		listService;

	@Autowired
	private AirlineManagerFlightShowService		showService;

	@Autowired
	private AirlineManagerFlightUpdateService	editService;

	@Autowired
	private AirlineManagerFlightCreateService	createService;

	@Autowired
	private AirlineManagerFlightPublishService	publishService;

	@Autowired
	private AirlineManagerFlightDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.editService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
