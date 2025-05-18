
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.AirlineType;
import acme.datatypes.AirportScope;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Map<AirportScope, Long>		totalNumberOfAirportsByOperationalScope;
	Map<AirlineType, Long>		numberOfAirlinesByType;

	Double						ratioOfAirlinesWithEmailAddressAndPhoneNumber;
	Double						ratioOfActiveAircrafts;
	Double						ratioOfNonActiveAircrafts;
	Double						ratioOfReviewsWithScoreAboveFive;

	Long						numberOfReviewsPostedInLastTenWeeks;
	Double						averageNumberOfReviewsPostedInLastTenWeeks;
	Long						minimumNumberOfReviewsPostedInLastTenWeeks;
	Long						maximumNumberOfReviewsPostedInLastTenWeeks;
	Double						standardDeviationOfNumberOfReviewsPostedInLastTenWeeks;
}
