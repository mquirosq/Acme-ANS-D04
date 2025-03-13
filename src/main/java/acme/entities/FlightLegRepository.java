
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightLegRepository extends AbstractRepository {

	@Query("select fl from FlightLeg fl where fl.scheduledArrival = (select min(fl2.scheduledArrival) from FlightLeg fl2 where fl2.parentFlight = :flightId)")
	FlightLeg getFirstLegOfFlight(Integer flightId);

	@Query("select fl from FlightLeg fl where fl.scheduledArrival = (select max(fl2.scheduledArrival) from FlightLeg fl2 where fl2.parentFlight = :flightId)")
	FlightLeg getLastLegOfFlight(Integer flightId);

	@Query("select count(fl) from FlightLeg fl where fl.parentFlight = :flightId")
	Integer getLegsCountOfFlight(Integer flightId);

}
