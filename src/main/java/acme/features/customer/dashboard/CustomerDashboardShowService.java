
package acme.features.customer.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TravelClass;
import acme.entities.Flight;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Date oneYearAgo = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);
		Date fiveYearsAgo = MomentHelper.deltaFromCurrentMoment(-5, ChronoUnit.YEARS);
		CustomerDashboard dashboard;

		List<String> lastFiveDestinations;
		List<Money> moneySpentInBookingsDuringLastYear;
		Map<TravelClass, Long> numberOfBookingsByTravelClass;

		List<Money> totalCostOfBookings;
		List<Money> averageCostOfBookings;
		List<Money> minimumCostOfBookings;
		List<Money> maximumCostOfBookings;
		List<Money> standardDeviationOfCostOfBookings;

		Long totalNumberOfPassengersInBookings;
		Double averageNumberOfPassengerInBookings;
		Long minimumNumberOfPassengersInBookings;
		Long maximumNumberOfPassengersInBookings;
		Double standardDeviationOfPassengersInBookings;

		PageRequest topFive = PageRequest.of(0, 5, Sort.by("purchasedAt").descending());
		lastFiveDestinations = this.repository.getFlightsOrdered(customerId, topFive).stream().map(Flight::getDestinationCity).toList();

		moneySpentInBookingsDuringLastYear = this.repository.getTotalCostOfBookingsSinceDate(oneYearAgo, customerId).stream().map(row -> {
			Money money = new Money();
			money.setAmount((Double) row[1]);
			money.setCurrency((String) row[0]);
			return money;
		}).toList();

		numberOfBookingsByTravelClass = this.repository.getNumberOfBookingsGroupedByTravelClass(customerId).stream().collect(Collectors.toMap(row -> (TravelClass) row[0], row -> (Long) row[1]));

		totalCostOfBookings = this.repository.getTotalCostOfBookingsSinceDate(fiveYearsAgo, customerId).stream().map(row -> {
			Money money = new Money();
			money.setAmount((Double) row[1]);
			money.setCurrency((String) row[0]);
			return money;
		}).toList();

		averageCostOfBookings = this.repository.getAverageCostOfBookingsSinceDate(fiveYearsAgo, customerId).stream().map(row -> {
			Money money = new Money();
			money.setAmount((Double) row[1]);
			money.setCurrency((String) row[0]);
			return money;
		}).toList();

		minimumCostOfBookings = this.repository.getMinimumCostOfBookingsSinceDate(fiveYearsAgo, customerId).stream().map(row -> {
			Money money = new Money();
			money.setAmount((Double) row[1]);
			money.setCurrency((String) row[0]);
			return money;
		}).toList();

		maximumCostOfBookings = this.repository.getMaximumCostOfBookingsSinceDate(fiveYearsAgo, customerId).stream().map(row -> {
			Money money = new Money();
			money.setAmount((Double) row[1]);
			money.setCurrency((String) row[0]);
			return money;
		}).toList();

		standardDeviationOfCostOfBookings = this.repository.getStandardDeviationCostOfBookingsSinceDate(fiveYearsAgo, customerId).stream().map(row -> {
			Money money = new Money();
			money.setAmount((Double) row[1]);
			money.setCurrency((String) row[0]);
			return money;
		}).toList();

		totalNumberOfPassengersInBookings = this.repository.getTotalPassengersInBookings(customerId);
		averageNumberOfPassengerInBookings = this.repository.getAverageNumberOfPassengersInBooking(customerId);
		minimumNumberOfPassengersInBookings = this.repository.getMinimumNumberOfPassengersInBooking(customerId);
		maximumNumberOfPassengersInBookings = this.repository.getMaximumNumberOfPassengersInBooking(customerId);

		Collection<Long> passengersByBooking = this.repository.getPassengersByBooking(customerId);
		double sumOfSquaredDifferences = passengersByBooking.stream().mapToDouble(value -> Math.pow(value - averageNumberOfPassengerInBookings, 2)).sum();
		double variance = sumOfSquaredDifferences / passengersByBooking.size();
		standardDeviationOfPassengersInBookings = Math.sqrt(variance);

		dashboard = new CustomerDashboard();
		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setMoneySpentInBookingsDuringLastYear(moneySpentInBookingsDuringLastYear);
		dashboard.setNumberOfBookingsByTravelClass(numberOfBookingsByTravelClass);

		dashboard.setTotalCostOfBookings(totalCostOfBookings);
		dashboard.setAverageCostOfBookings(averageCostOfBookings);
		dashboard.setMinimumCostOfBookings(minimumCostOfBookings);
		dashboard.setMaximumCostOfBookings(maximumCostOfBookings);
		dashboard.setStandardDeviationOfCostOfBookings(standardDeviationOfCostOfBookings);

		dashboard.setTotalNumberOfPassengersInBookings(totalNumberOfPassengersInBookings);
		dashboard.setAverageNumberOfPassengerInBookings(averageNumberOfPassengerInBookings);
		dashboard.setMaximumNumberOfPassengersInBookings(maximumNumberOfPassengersInBookings);
		dashboard.setMinimumNumberOfPassengersInBookings(minimumNumberOfPassengersInBookings);
		dashboard.setStandardDeviationOfPassengersInBookings(standardDeviationOfPassengersInBookings);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, "lastFiveDestinations", "moneySpentInBookingsDuringLastYear", "numberOfBookingsByTravelClass", "totalCostOfBookings", "averageCostOfBookings", "minimumCostOfBookings", "maximumCostOfBookings",
			"standardDeviationOfCostOfBookings", "totalNumberOfPassengersInBookings", "averageNumberOfPassengerInBookings", "minimumNumberOfPassengersInBookings", "maximumNumberOfPassengersInBookings", "standardDeviationOfPassengersInBookings");

		super.getResponse().addData(dataset);
	}

}
