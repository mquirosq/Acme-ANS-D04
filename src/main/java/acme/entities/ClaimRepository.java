
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.datatypes.ClaimStatus;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("select count(t) from TrackingLog t where t.claim.id = :claimId and t.status = :status")
	public Long findAmountOfTrackingLogsByClaimIdAndStatus(Integer claimId, ClaimStatus status);
}
