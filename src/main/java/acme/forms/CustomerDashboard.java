
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

	private static final long		serialVersionUID	= 1L;

	private List<String>			lastFiveDestinations;
	private List<Money>				moneySpentInBookingsDuringLastYear;
	private Map<TravelClass, Long>	numberOfBookingsByTravelClass;

	private List<Money>				totalCostOfBookings;
	private List<Money>				averageCostOfBookings;
	private List<Money>				minimumCostOfBookings;
	private List<Money>				maximumCostOfBookings;
	private List<Money>				standardDeviationOfCostOfBookings;

	private Long					totalNumberOfPassengersInBookings;
	private Double					averageNumberOfPassengerInBookings;
	private Long					minimumNumberOfPassengersInBookings;
	private Long					maximumNumberOfPassengersInBookings;
	private Double					standardDeviationOfPassengersInBookings;
}
