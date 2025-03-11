
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	List<String>				lastFiveDestionations;
	Money						moneySpentInBookingDuringLastYear;
	Map<String, Integer>		numberOfBookingsByTravelClass;
	Integer						numberOfBookings;
	Double						averageCostOfBookings;
	Double						minimumCostOfBookings;
	Double						maximumOfCostOfBookings;
	Double						standardDeviationOfCostOfBookings;
	Integer						totalNumberOfPassengersInBookings;
	Double						averageNumberOfPassengerInBookings;
	Double						minimumNumberOfPassengersInBookings;
	Double						maximumNumberOfPassengersInBookings;
	Double						standardDeviationOfPassengersInBookings;
}
