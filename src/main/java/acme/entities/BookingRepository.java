
package acme.entities;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where locatorCode = :locatorCode")
	Booking getSameLocatorCode(String locatorCode);
}
