
package acme.entities;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking getSameLocatorCode(String locatorCode);

	@Query("select count(br) from BookingRecord br where br.booking.id = :bookingId")
	Long getNumberOfPassengers(int bookingId);
}
