
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface RecommendationRepository extends AbstractRepository {

	@Query("select r from Recommendation r where r.placeId = :placeId")
	Recommendation getSamePlaceId(String placeId);
}
