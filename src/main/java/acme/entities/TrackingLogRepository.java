
package acme.entities;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.datatypes.ClaimStatus;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.creationMoment asc")
	List<TrackingLog> findAllByClaimId(Integer claimId);

	@Query("select t from TrackingLog t where t.status = :status")
	List<TrackingLog> findAllByStatus(ClaimStatus status);
}
