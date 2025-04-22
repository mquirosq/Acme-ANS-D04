
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Flight;
import acme.entities.FlightLeg;

@Repository
public interface AirlineManagerFlightLegRepository extends AbstractRepository {

	@Query("select fl from FlightLeg fl where fl.parentFlight.id = :flightId")
	Collection<FlightLeg> findAllLegsByFlightId(int flightId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

}
