
package acme.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId and t.id != :trackingLogId and t.lastUpdateMoment < :lastUpdateMoment order by t.lastUpdateMoment desc, t.resolutionPercentage desc")
	List<TrackingLog> findAllByClaimIdWithDifferentIdBefore(Integer claimId, Integer trackingLogId, Date lastUpdateMoment);
}
