
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AircraftRepository extends AbstractRepository {

	@Query("select count(a) from Aircraft a where a.registrationNumber = :registrationNumber")
	Long countWithSameRegistrationNumber(String registrationNumber);
}
