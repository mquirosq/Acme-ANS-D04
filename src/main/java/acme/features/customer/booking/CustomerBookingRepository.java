
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Booking;
import acme.entities.Flight;
import acme.realms.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(int customerId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(int customerId);

	@Query("select count(br) from BookingRecord br where br.booking.id = :bookingId")
	Long countPassengersInBooking(int bookingId);
}
