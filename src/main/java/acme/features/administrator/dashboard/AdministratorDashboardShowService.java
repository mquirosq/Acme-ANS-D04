
package acme.features.administrator.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AirlineType;
import acme.datatypes.AirportScope;
import acme.forms.AdministratorDashboard;

@GuiService
public class AdministratorDashboardShowService extends AbstractGuiService<Administrator, AdministratorDashboard> {

	private final AdministratorDashboardRepository repository;


	@Autowired
	public AdministratorDashboardShowService(final AdministratorDashboardRepository repository) {
		this.repository = repository;
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AdministratorDashboard dashboard;

		Double ratioOfAirlinesWithEmailAddressAndPhoneNumber;
		Double ratioOfActiveAircrafts;
		Double ratioOfNonActiveAircrafts;
		Double ratioOfReviewsWithScoreAboveFive;
		Double averageNumberOfReviewsPostedInLastTenWeeks;
		Double varianceOfNumberOfReviewsPostedInLastTenWeeks;
		Double standardDeviationOfNumberOfReviewsPostedInLastTenWeeks;
		Long numberOfReviewsPostedInLastTenWeeks;
		Long minimumNumberOfReviewsPostedInLastTenWeeks;
		Long maximumNumberOfReviewsPostedInLastTenWeeks;

		Map<AirportScope, Long> totalNumberOfAirportsByOperationalScope;
		Map<AirlineType, Long> numberOfAirlinesByType;
		List<Long> weeklyNumberOfReviews;

		/* Calculate form data using repository */
		totalNumberOfAirportsByOperationalScope = this.repository.totalNumberOfAirportsByOperationalScope().stream().collect(Collectors.toMap(pair -> (AirportScope) pair[0], pair -> (Long) pair[1]));
		numberOfAirlinesByType = this.repository.numberOfAirlinesByType().stream().collect(Collectors.toMap(pair -> (AirlineType) pair[0], pair -> (Long) pair[1]));

		ratioOfAirlinesWithEmailAddressAndPhoneNumber = this.repository.ratioOfAirlinesWithEmailAddressAndPhoneNumber();
		ratioOfActiveAircrafts = this.repository.ratioOfActiveAircrafts();
		ratioOfNonActiveAircrafts = this.repository.ratioOfNonActiveAircrafts();
		ratioOfReviewsWithScoreAboveFive = this.repository.ratioOfReviewsWithScoreAboveFive();

		weeklyNumberOfReviews = new ArrayList<>();

		for (Integer i = 1; i <= 10; i++) {
			Long numReviews;
			Date start;
			Date end;

			start = MomentHelper.deltaFromCurrentMoment(-i, ChronoUnit.WEEKS);
			end = MomentHelper.deltaFromCurrentMoment(-i + 1L, ChronoUnit.WEEKS);

			numReviews = this.repository.numberOfReviewsPostedInWeek(start, end);
			weeklyNumberOfReviews.add(numReviews);
		}
		numberOfReviewsPostedInLastTenWeeks = weeklyNumberOfReviews.stream().mapToLong(x -> x).sum();
		minimumNumberOfReviewsPostedInLastTenWeeks = weeklyNumberOfReviews.stream().mapToLong(x -> x).min().orElse(0L);
		maximumNumberOfReviewsPostedInLastTenWeeks = weeklyNumberOfReviews.stream().mapToLong(x -> x).max().orElse(0L);
		averageNumberOfReviewsPostedInLastTenWeeks = weeklyNumberOfReviews.stream().mapToLong(x -> x).average().orElse(0L);
		varianceOfNumberOfReviewsPostedInLastTenWeeks = weeklyNumberOfReviews.stream().mapToDouble(x -> Math.pow(x.doubleValue() - averageNumberOfReviewsPostedInLastTenWeeks, 2.0)).sum() / (weeklyNumberOfReviews.size() - 1);
		standardDeviationOfNumberOfReviewsPostedInLastTenWeeks = Math.sqrt(varianceOfNumberOfReviewsPostedInLastTenWeeks);

		/* Assign form data */
		dashboard = new AdministratorDashboard();

		dashboard.setTotalNumberOfAirportsByOperationalScope(totalNumberOfAirportsByOperationalScope);
		dashboard.setNumberOfAirlinesByType(numberOfAirlinesByType);

		dashboard.setRatioOfAirlinesWithEmailAddressAndPhoneNumber(ratioOfAirlinesWithEmailAddressAndPhoneNumber);
		dashboard.setRatioOfActiveAircrafts(ratioOfActiveAircrafts);
		dashboard.setRatioOfNonActiveAircrafts(ratioOfNonActiveAircrafts);
		dashboard.setRatioOfReviewsWithScoreAboveFive(ratioOfReviewsWithScoreAboveFive);

		dashboard.setNumberOfReviewsPostedInLastTenWeeks(numberOfReviewsPostedInLastTenWeeks);
		dashboard.setMinimumNumberOfReviewsPostedInLastTenWeeks(minimumNumberOfReviewsPostedInLastTenWeeks);
		dashboard.setMaximumNumberOfReviewsPostedInLastTenWeeks(maximumNumberOfReviewsPostedInLastTenWeeks);
		dashboard.setAverageNumberOfReviewsPostedInLastTenWeeks(averageNumberOfReviewsPostedInLastTenWeeks);
		dashboard.setStandardDeviationOfNumberOfReviewsPostedInLastTenWeeks(standardDeviationOfNumberOfReviewsPostedInLastTenWeeks);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, "totalNumberOfAirportsByOperationalScope", "numberOfAirlinesByType", "ratioOfAirlinesWithEmailAddressAndPhoneNumber", "ratioOfActiveAircrafts", "ratioOfNonActiveAircrafts", "ratioOfReviewsWithScoreAboveFive",
			"numberOfReviewsPostedInLastTenWeeks", "averageNumberOfReviewsPostedInLastTenWeeks", "minimumNumberOfReviewsPostedInLastTenWeeks", "maximumNumberOfReviewsPostedInLastTenWeeks", "standardDeviationOfNumberOfReviewsPostedInLastTenWeeks");

		super.getResponse().addData(dataset);
	}
}
