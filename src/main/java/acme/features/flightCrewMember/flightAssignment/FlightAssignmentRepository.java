
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.FlightAssignment;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.moment <= :moment and f.allocatedFlightCrewMember.id = :id")
	Collection<FlightAssignment> findCompletedFlightAssignments(Date moment, int id);

	@Query("select f from FlightAssignment f where f.moment > :moment and f.allocatedFlightCrewMember.id = :id")
	Collection<FlightAssignment> findPlannedFlightAssignments(Date moment, int id);
}
