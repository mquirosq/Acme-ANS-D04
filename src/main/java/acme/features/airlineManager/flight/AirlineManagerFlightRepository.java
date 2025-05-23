
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.ActivityLog;
import acme.entities.Flight;
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select sc.systemCurrency from SystemConfiguration sc")
	String getSystemCurrency();

	@Query("select f from Flight f where f.manager.id = :airlineManagerId")
	Collection<Flight> findAllFlightsByAirlineManagerId(int airlineManagerId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select am from AirlineManager am where am.id = :managerId")
	AirlineManager findManagerById(int managerId);

	@Query("select fl from FlightLeg fl where fl.parentFlight.id = :flightId")
	Collection<FlightLeg> findAllLegsByFlightId(int flightId);

	@Query("select fa from FlightAssignment fa where fa.leg.id in (select fl.id from FlightLeg fl where fl.parentFlight.id = :flightId)")
	Collection<FlightAssignment> findAllAssignmentsByFlightId(int flightId);

	@Query("select al from ActivityLog al where al.assignment.id in (select fa.id from FlightAssignment fa where fa.leg.id in (select fl.id from FlightLeg fl where fl.parentFlight.id = :flightId))")
	Collection<ActivityLog> findAllActivityLogsByFlightId(int flightId);

}
