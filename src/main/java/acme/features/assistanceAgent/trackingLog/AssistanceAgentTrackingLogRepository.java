
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Claim;
import acme.entities.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :masterId order by t.id asc")
	Collection<TrackingLog> findAllTrackingLogsByMasterId(int masterId);

	@Query("select t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

}
