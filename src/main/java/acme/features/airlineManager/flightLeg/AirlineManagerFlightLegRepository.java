
package acme.features.airlineManager.flightLeg;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.ActivityLog;
import acme.entities.Aircraft;
import acme.entities.Airport;
import acme.entities.Flight;
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerFlightLegRepository extends AbstractRepository {

	@Query("select fl from FlightLeg fl where fl.parentFlight.id = :flightId order by fl.scheduledArrival asc")
	Collection<FlightLeg> findAllLegsByFlightId(int flightId);

	@Query("select fl from FlightLeg fl where (fl.parentFlight.id = :flightId and fl.draftMode = false) order by fl.scheduledArrival asc")
	List<FlightLeg> getPublishedLegsOfFlightOrderedByArrival(Integer flightId);

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

	@Query("select fa from FlightAssignment fa where fa.leg.id = :legId")
	Collection<FlightAssignment> findAllAssignmentsByFlightLegId(int legId);

	@Query("select al from ActivityLog al where al.assignment.leg.id = :legId")
	Collection<ActivityLog> findAllActivityLogsByFlightLegId(int legId);

}
