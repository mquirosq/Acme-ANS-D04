
package acme.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId and t.id != :trackingLogId and t.creationMoment < :creationMoment order by t.creationMoment desc, t.resolutionPercentage desc")
	List<TrackingLog> findAllByClaimIdWithDifferentIdBefore(Integer claimId, Integer trackingLogId, Date creationMoment);

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.creationMoment desc")
	List<TrackingLog> findAllByClaimId(Integer claimId);
}
