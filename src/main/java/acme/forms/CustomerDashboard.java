
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.datatypes.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	List<String>				lastFiveDestinations;
	List<Money>					moneySpentInBookingsDuringLastYear;
	Map<TravelClass, Long>		numberOfBookingsByTravelClass;

	List<Money>					totalCostOfBookings;
	List<Money>					averageCostOfBookings;
	List<Money>					minimumCostOfBookings;
	List<Money>					maximumCostOfBookings;
	List<Money>					standardDeviationOfCostOfBookings;

	Long						totalNumberOfPassengersInBookings;
	Double						averageNumberOfPassengerInBookings;
	Long						minimumNumberOfPassengersInBookings;
	Long						maximumNumberOfPassengersInBookings;
	Double						standardDeviationOfPassengersInBookings;
}
