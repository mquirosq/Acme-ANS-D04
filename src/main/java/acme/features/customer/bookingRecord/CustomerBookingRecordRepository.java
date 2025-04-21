
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Booking;
import acme.entities.BookingRecord;
import acme.entities.Passenger;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	Collection<BookingRecord> findBookingRecordsByBookingId(int bookingId);

	@Query("select br.booking from BookingRecord br where br.id = :id")
	Booking findBookingOfBookingRecordById(int id);

	@Query("select br from BookingRecord br where br.id = :id")
	BookingRecord findBookingRecordById(int id);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select b.draftMode from Booking b where b.id = :bookingId")
	boolean findBookingDraftById(int bookingId);

	@Query("select p from Passenger p where p.customer.id = :customerId and p.draftMode = false and p not in (select br.passenger from BookingRecord br where br.booking.id = :bookingId)")
	Collection<Passenger> findMyPassengersNotAlreadyInBooking(int customerId, int bookingId);

	@Query("select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(int passengerId);
}
