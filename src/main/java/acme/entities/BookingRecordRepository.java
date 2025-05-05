
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface BookingRecordRepository extends AbstractRepository {

	@Query("select br from BookingRecord br where br.booking = :booking and br.passenger = :passenger")
	BookingRecord getSameBookingAndPassenger(Passenger passenger, Booking booking);
}
