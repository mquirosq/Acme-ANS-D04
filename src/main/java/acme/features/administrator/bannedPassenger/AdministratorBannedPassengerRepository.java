
package acme.features.administrator.bannedPassenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.BannedPassenger;

@Repository
public interface AdministratorBannedPassengerRepository extends AbstractRepository {

	@Query("select p from BannedPassenger p where p.id = :bannedId")
	BannedPassenger findBannedPassengerById(Integer bannedId);

	@Query("select p from BannedPassenger p where p.liftedAt IS NULL")
	Collection<BannedPassenger> findCurrentlyBannedPassengers();

}
