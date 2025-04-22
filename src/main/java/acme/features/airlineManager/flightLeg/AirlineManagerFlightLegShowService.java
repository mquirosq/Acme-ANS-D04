
package acme.features.airlineManager.flightLeg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.FlightLeg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegShowService extends AbstractGuiService<AirlineManager, FlightLeg> {

	@Autowired
	private AirlineManagerFlightLegRepository repository;

	/*
	 * 
	 * @Override
	 * public void authorise() {
	 * super.getResponse().setAuthorised(true);
	 * }
	 * 
	 * @Override
	 * public void load() {
	 * Flight flight;
	 * int id;
	 * 
	 * id = super.getRequest().getData("id", int.class);
	 * flight = this.repository.findFlightById(id);
	 * 
	 * super.getBuffer().addData(flight);
	 * }
	 * 
	 * @Override
	 * public void unbind(final Flight flight) {
	 * Dataset dataset;
	 * Date scheduledDeparture = flight.getScheduledDeparture();
	 * Date scheduledArrival = flight.getScheduledArrival();
	 * String originCity = flight.getOriginCity();
	 * String destinationCity = flight.getDestinationCity();
	 * Integer numberOfLayovers = flight.getNumberOfLayovers();
	 * 
	 * dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
	 * 
	 * dataset.put("scheduledDeparture", scheduledDeparture != null ? scheduledDeparture : "-");
	 * dataset.put("scheduledArrival", scheduledArrival != null ? scheduledArrival : "-");
	 * dataset.put("originCity", originCity != null ? originCity : "-");
	 * dataset.put("destinationCity", destinationCity != null ? destinationCity : "-");
	 * dataset.put("numberOfLayovers", numberOfLayovers);
	 * 
	 * super.getResponse().addData(dataset);
	 * }
	 * 
	 */

}
