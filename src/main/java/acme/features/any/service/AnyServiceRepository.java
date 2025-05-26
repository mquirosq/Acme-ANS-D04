
package acme.features.any.service;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Service;

@Repository
public interface AnyServiceRepository extends AbstractRepository {

	@Query("select s from Service s where s.id = :id")
	Service findServiceById(int id);

	@Query("select s from Service s")
	Collection<Service> findAllServices();

}
