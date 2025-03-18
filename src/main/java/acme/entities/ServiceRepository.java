
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ServiceRepository extends AbstractRepository {

	@Query("select count(s) from Service s where s.promotionCode = :promotionCode")
	Integer getEqual(String promotionCode);
}
