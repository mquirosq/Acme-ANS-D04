
package acme.helpers;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.SpringHelper;
import acme.datatypes.FlightLegStatus;
import acme.entities.Aircraft;
import acme.entities.Airport;
import acme.entities.Flight;
import acme.entities.FlightLeg;
import acme.features.airlineManager.flightLeg.AirlineManagerFlightLegRepository;

public class FlightHelper {

	public static List<SelectChoices> getFlightLegFormChoices(final FlightLeg leg, final int managerId) {
		Collection<Flight> flights;
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;

		SelectChoices statusChoices, arrivalAirportChoices, departureAirportChoices, aircraftChoices, flightChoices;

		AirlineManagerFlightLegRepository flightLegRepository = SpringHelper.getBean(AirlineManagerFlightLegRepository.class);

		flights = flightLegRepository.findAllFlightsByAirlineManagerId(managerId);
		airports = flightLegRepository.findAllAirports();
		aircrafts = flightLegRepository.findAllAircrafts();

		statusChoices = SelectChoices.from(FlightLegStatus.class, leg.getStatus());
		arrivalAirportChoices = SelectChoices.from(airports, "IATACode", leg.getArrivalAirport());
		departureAirportChoices = SelectChoices.from(airports, "IATACode", leg.getDepartureAirport());
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getDeployedAircraft());
		flightChoices = SelectChoices.from(flights, "tag", leg.getParentFlight());

		return List.of(statusChoices, arrivalAirportChoices, departureAirportChoices, aircraftChoices, flightChoices);
	}

	public static Dataset unbindFlightDerivatedProperties(final Dataset dataset, final Flight flight) {
		Date scheduledDeparture = flight.getScheduledDeparture();
		Date scheduledArrival = flight.getScheduledArrival();
		String originCity = flight.getOriginCity();
		String destinationCity = flight.getDestinationCity();
		Integer numberOfLayovers = flight.getNumberOfLayovers();

		dataset.put("scheduledDeparture", scheduledDeparture != null ? scheduledDeparture : "-");
		dataset.put("scheduledArrival", scheduledArrival != null ? scheduledArrival : "-");
		dataset.put("originCity", originCity != null ? originCity : "-");
		dataset.put("destinationCity", destinationCity != null ? destinationCity : "-");
		dataset.put("numberOfLayovers", numberOfLayovers);

		return dataset;
	}

}
