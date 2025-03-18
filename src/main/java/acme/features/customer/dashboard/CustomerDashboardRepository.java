
package acme.features.customer.dashboard;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select b.flight from Booking b where b.customer.id = :customerId order by b.purchasedAt desc")
	Collection<String> getFlightsOrderByRecentBooking(Integer customerId);

	@Query("select b.travelClass, count(b) from Booking b where b.customer.id = :customerId group by b.travelClass")
	Collection<Object[]> getNumberOfBookingsGroupedByTravelClass(Integer customerId);

	@Query("select sum(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId")
	Double getTotalCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select avg(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId")
	Double getAverageCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select min(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId")
	Double getMinimumCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select max(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId")
	Double maximumCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select stddev(b.price.amount) from Booking b where b.purchasedAt >= :startDate and b.customer.id = :customerId")
	Double standardDeviationCostOfBookingsSinceDate(Date startDate, Integer customerId);

	@Query("select count(br) from BookingRecord br where br.booking.customer=:customerId")
	Long getTotalPassengersInBookings(Integer customerId);

	@Query("select avg(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId")
	Double getAverageNumberOfPassengersInBooking(Integer customerId);

	@Query("select min(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId")
	Long getMinimumNumberOfPassengersInBooking(Integer customerId);

	@Query("select max(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId")
	Long getMaximumNumberOfPassengersInBooking(Integer customerId);

	@Query("select count(br) from BookingRecord br group by br.booking.id where b.customer.id = :customerId")
	Collection<Long> getPassengersByBooking(Integer customerId);

}
