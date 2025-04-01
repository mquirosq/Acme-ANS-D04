
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select br.passenger from BookingRecord br where br.booking.customer.id = :customerId")
	Collection<Passenger> findPassengersOfCustomer(int customerId);

	@Query("select p from Passenger p where p.id = :id")
	Passenger findPassengerById(int id);

	@Query("select p from Passenger p where p.customer.id = :customerId and p.draftMode = true")
	Collection<Passenger> findDraftPassengersOfCustomer(int customerId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(int customerId);

}
