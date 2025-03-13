
package acme.features.customer.dashboard;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select b.flight from Booking b where b.customer.id = :customerId order by b.purchasedAt desc")
	Collection<String> getFlightsOrderByRecentBooking(@Param("customerId") Integer customerId);

	@Query("select b.travelClass, count(b) from Booking b where b.customer.id = :customerId group by b.travelClass")
	Collection<Object[]> getNumberOfBookingsGroupedByTravelClass(@Param("customerId") Integer customerId);

	@Query("select sum(b.price.amount) from Booking b where b.purchasedAt >= :startDate")
	Double getTotalCostOfBookingsSinceDate(@Param("startDate") Date startDate);

	@Query("select avg(b.price.amount) from Booking b where b.purchasedAt >= :startDate")
	Double getAverageCostOfBookingsSinceDate(@Param("startDate") Date startDate);

	@Query("select min(b.price.amount) from Booking b where b.purchasedAt >= :startDate")
	Double getMinimumCostOfBookingsSinceDate(@Param("startDate") Date startDate);

	@Query("select max(b.price.amount) from Booking b where b.purchasedAt >= :startDate")
	Double maximumCostOfBookingsSinceDate(@Param("startDate") Date startDate);

	@Query("select stddev(b.price.amount) from Booking b where b.purchasedAt >= :startDate")
	Double standardDeviationCostOfBookingsSinceDate(@Param("startDate") Date startDate);

	@Query("select count(br) from BookingRecord br where br.booking.customer=:customerId")
	Long getTotalPassengersInBookings(@Param("customerId") Integer customerId);

	@Query("select avg(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId")
	Double getAverageNumberOfPassengersInBooking(@Param("customerId") Integer customerId);

	@Query("select min(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId")
	Long getMinimumNumberOfPassengersInBooking(@Param("customerId") Integer customerId);

	@Query("select max(select count(br) from BookingRecord br where br.booking.id = b.id) from Booking b where b.customer.id = :customerId")
	Long getMaximumNumberOfPassengersInBooking(@Param("customerId") Integer customerId);

	// Standard deviation number of passengers in bookings
}
