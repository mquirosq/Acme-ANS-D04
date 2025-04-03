
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Flight;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.manager.id = :airlineManagerId")
	Collection<Flight> findAllFlightsByAirlineManagerId(int airlineManagerId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select am from AirlineManager am")
	Collection<AirlineManager> findAllAirlineManagers();

}
