
package acme.entities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightLegRepository extends AbstractRepository {

	@Query("select fl from FlightLeg fl where fl.scheduledArrival = (select min(fl2.scheduledArrival) from FlightLeg fl2 where fl2.parentFlight.id = :flightId)")
	Optional<FlightLeg> getFirstLegOfFlight(Integer flightId);

	@Query("select fl from FlightLeg fl where fl.scheduledArrival = (select max(fl2.scheduledArrival) from FlightLeg fl2 where fl2.parentFlight.id = :flightId)")
	Optional<FlightLeg> getLastLegOfFlight(Integer flightId);

	@Query("select count(fl) from FlightLeg fl where fl.parentFlight.id = :flightId")
	Integer getLegsCountOfFlight(Integer flightId);

	@Query("select fl from FlightLeg fl where fl.parentFlight.id = :flightId order by fl.scheduledArrival asc")
	List<FlightLeg> getLegsOfFlight(Integer flightId);

	@Query("select fl from FlightLeg fl where fl.flightNumber = :flightNumber")
	FlightLeg getByFlightNumber(String flightNumber);

}
