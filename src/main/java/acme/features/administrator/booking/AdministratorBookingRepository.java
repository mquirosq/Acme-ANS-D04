
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Booking;
import acme.entities.Flight;
import acme.realms.Customer;

@Repository
public interface AdministratorBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.draftMode = false")
	Collection<Booking> findNonDraftBookings();

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select f from Flight f where f.draftMode = false")
	Collection<Flight> findAllNonDraftFlights();

	@Query("select c from Customer c")
	Collection<Customer> findAllCustomers();
}
