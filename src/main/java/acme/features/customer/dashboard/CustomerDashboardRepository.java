
package acme.features.customer.dashboard;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.Flight;

public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select b.flight from Booking b where b.customer.id = :customerId and b.draftMode = false")
	List<Flight> getFlightsOrdered(Integer customerId, Pageable page);

	@Query("select b.price.currency, sum(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId and b.draftMode = false group by b.price.currency")
	Collection<Object[]> getTotalCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select b.travelClass, count(b) from Booking b where b.customer.id = :customerId and b.draftMode = false group by b.travelClass")
	Collection<Object[]> getNumberOfBookingsGroupedByTravelClass(Integer customerId);

	@Query("select b.price.currency, avg(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId and b.draftMode = false group by b.price.currency")
	Collection<Object[]> getAverageCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select  b.price.currency, min(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId and b.draftMode = false group by b.price.currency")
	Collection<Object[]> getMinimumCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select  b.price.currency, max(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId and b.draftMode = false group by b.price.currency")
	Collection<Object[]> getMaximumCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select  b.price.currency, stddev(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId and b.draftMode = false group by b.price.currency")
	Collection<Object[]> getStandardDeviationCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select count(br) from BookingRecord br where br.booking.customer.id=:customerId and br.booking.draftMode = false")
	Long getTotalPassengersInBookings(Integer customerId);

	@Query("select avg(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId and b.draftMode = false")
	Double getAverageNumberOfPassengersInBooking(Integer customerId);

	@Query("select min(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId and b.draftMode = false")
	Long getMinimumNumberOfPassengersInBooking(Integer customerId);

	@Query("select max(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId and b.draftMode = false")
	Long getMaximumNumberOfPassengersInBooking(Integer customerId);

	@Query("select count(br) from BookingRecord br where br.booking.customer.id = :customerId and br.booking.draftMode = false group by br.booking.id")
	Collection<Long> getPassengersByBooking(Integer customerId);
}
