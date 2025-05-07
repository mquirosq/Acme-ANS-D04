
package acme.realms;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineManagerRepository extends AbstractRepository {

	@Query("select a from AirlineManager a where a.identifierNumber = :identifierNumber")
	public AirlineManager findByIdentifierNumber(String identifierNumber);

}
