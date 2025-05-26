
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

	private static final long		serialVersionUID	= 1L;

	private Map<AirportScope, Long>	totalNumberOfAirportsByOperationalScope;
	private Map<AirlineType, Long>	numberOfAirlinesByType;

	private Double					ratioOfAirlinesWithEmailAddressAndPhoneNumber;
	private Double					ratioOfActiveAircrafts;
	private Double					ratioOfNonActiveAircrafts;
	private Double					ratioOfReviewsWithScoreAboveFive;

	private Long					numberOfReviewsPostedInLastTenWeeks;
	private Double					averageNumberOfReviewsPostedInLastTenWeeks;
	private Long					minimumNumberOfReviewsPostedInLastTenWeeks;
	private Long					maximumNumberOfReviewsPostedInLastTenWeeks;
	private Double					standardDeviationOfNumberOfReviewsPostedInLastTenWeeks;
}
