
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.ActivityLog;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select a from ActivityLog a")
	Collection<ActivityLog> findAllActivityLogs();

	@Query("select a from ActivityLog a where a.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("select a from ActivityLog a where a.assignment.id = :id")
	Collection<ActivityLog> findByFlightAssignmentId(int id);

}
