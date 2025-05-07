
package acme.features.customer.recommendation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Flight;
import acme.entities.Recommendation;

@Repository
public interface CustomerRecommendationRepository extends AbstractRepository {

	@Query("select r from Recommendation r where r.id = :recommendationId")
	Recommendation findRecommendationById(int recommendationId);

	@Query("select b.flight from Booking b where b.customer.id = :customerId")
	List<Flight> findFlightsOfCustomer(int customerId);

	@Query("select r from Recommendation r where r.country = :country and r.city = :city")
	List<Recommendation> getRecommendationsForCityAndCountry(String city, String country);
}
