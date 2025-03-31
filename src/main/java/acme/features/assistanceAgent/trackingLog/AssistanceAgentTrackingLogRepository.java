
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Claim;
import acme.entities.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.agent.id = :id")
	Collection<TrackingLog> findAllTrackingLogsByAssistanceAgentId(int id);

	@Query("select t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("select c from Claim c where c.agent.id = :id")
	Collection<Claim> findAllClaimsByAssistanceAgentId(int id);
}
