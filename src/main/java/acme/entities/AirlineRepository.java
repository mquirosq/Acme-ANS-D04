
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("select count(a) from Airline a where IATACode = :IATACode")
	Long countSameIATACode(String IATACode);

}
