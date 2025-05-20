
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.datatypes.FlightLegStatus;
import acme.entities.ActivityLog;
import acme.entities.FlightAssignment;
import acme.entities.FlightLeg;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.leg.status = :status and f.allocatedFlightCrewMember.id = :id")
	Collection<FlightAssignment> findCompletedFlightAssignments(FlightLegStatus status, int id);

	@Query("select f from FlightAssignment f where f.leg.status != :status and f.allocatedFlightCrewMember.id = :id")
	Collection<FlightAssignment> findPlannedFlightAssignments(FlightLegStatus status, int id);

	@Query("select f from FlightAssignment f where f.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from FlightLeg l")
	Collection<FlightLeg> findAllLegs();

	@Query("select l from FlightLeg l where l.draftMode = false")
	Collection<FlightLeg> findAllPublishedLegs();

	@Query("select f from FlightCrewMember f")
	Collection<FlightCrewMember> findAllFlightCrewMembers();

	@Query("select l from FlightLeg l where l.id = :id")
	FlightLeg findByLegId(int id);

	@Query("select f from FlightCrewMember f where f.id = :id")
	FlightCrewMember findByFlightCrewMemberId(int id);

	@Query("select f from FlightAssignment f where f.allocatedFlightCrewMember.id = :id ")
	Collection<FlightAssignment> findFlightAssignmentsByFlightCrewMemberId(int id);

	@Query("select f from FlightAssignment f where f.leg.id = :id")
	Collection<FlightAssignment> findFlightAssignmentsByFlightLegId(int id);

	@Query("select a from ActivityLog a where a.assignment.id = :id")
	Collection<ActivityLog> findActivityLogsByFlightAssignmentId(int id);

}
