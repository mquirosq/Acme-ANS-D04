
package acme.features.customer.dashboard;

import acme.client.repositories.AbstractRepository;

public interface CustomerDashboardRepository extends AbstractRepository {

	// The last five destinations
	// @Query("select b.flight.destination from Booking b where b.customer.id = :customerId order by b.purchasedAt desc")
	// List<String> destinationsOrderByRecent(@Param("customerId") Integer customerId);

	// The money spent in bookings during the last year
	// @Query("select sum(b.costValue) from Booking b where b.purchasedAt >= :startDate && b.customer.id = :customerId")
	// Double totalMoneySpentLastYear(@Param("customerId") Integer customerId, @Param("startDate") LocalDate startDate);

	// The number of bookings grouped by travel class
	// @Query("select b.travelClass, count(b) from Booking b where b.customer.id = :customerId group by b.travelClass")
	// List<Object[]> numberOfBookingsGroupedByTravelClass(@Param("customerId") Integer customerId);

	// Total cost of bookings in last 5 years ??
	// @Query("select sum(cost) from Booking b")

	// Average cost of bookings in last 5 years

	// Minimum cost of bookings in last 5 years

	// Maximum cost of bookings in last 5 years

	// Standard deviation cost of bookings in last 5 years

	// Total number of passengers in bookings

	// Average number of passengers in bookings

	// Minimum number of passengers in bookings

	// Maximum number of passengers in bookings

	// Standard deviation number of passengers in bookings

}
