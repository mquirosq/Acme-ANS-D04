
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Aircraft;
import acme.entities.Airport;
import acme.entities.Flight;
import acme.entities.FlightLeg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerFlightLegRepository extends AbstractRepository {

	@Query("select fl from FlightLeg fl where fl.parentFlight.id = :flightId")
	Collection<FlightLeg> findAllLegsByFlightId(int flightId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select fl from FlightLeg fl where fl.id = :legId")
	FlightLeg findFlightLegById(int legId);

	@Query("select am from AirlineManager am where am.id = :managerId")
	AirlineManager findManagerById(int managerId);

	@Query("select f from Flight f where f.manager.id = :airlineManagerId")
	Collection<Flight> findAllFlightsByAirlineManagerId(int airlineManagerId);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

}
