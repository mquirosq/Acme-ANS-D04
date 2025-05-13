
package acme.features.administrator.recommendation;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.Recommendation;

public interface AdministratorRecommendationRepository extends AbstractRepository {

	@Query("select r from Recommendation r where r.placeId = :placeId")
	Recommendation getRecommendationByPlaceId(String placeId);
}
