
package acme.features.airlineManager.flightLeg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.FlightLeg;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerFlightLegController extends AbstractGuiController<AirlineManager, FlightLeg> {

	@Autowired
	private AirlineManagerFlightLegListService		listService;

	@Autowired
	private AirlineManagerFlightLegShowService		showService;

	//@Autowired
	//private AirlineManagerFlightLegUpdateService	editService;

	@Autowired
	private AirlineManagerFlightLegCreateService	createService;

	//@Autowired
	//private AirlineManagerFlightLegPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		//super.addBasicCommand("update", this.editService);
		super.addBasicCommand("create", this.createService);
		//super.addCustomCommand("publish", "update", this.publishService);
	}

}
