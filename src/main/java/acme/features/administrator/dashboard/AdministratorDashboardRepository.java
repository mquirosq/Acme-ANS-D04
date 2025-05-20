
package acme.features.administrator.dashboard;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("select a.scope, count(a) from Airport a group by a.scope")
	Collection<Object[]> totalNumberOfAirportsByOperationalScope();

	@Query("select a.type, count(a) from Airline a group by a.type")
	Collection<Object[]> numberOfAirlinesByType();

	@Query("select 1.0 * count(a) / (select count(b) from Airline b) from Airline a where a.email != null and a.phoneNumber != null")
	Double ratioOfAirlinesWithEmailAddressAndPhoneNumber();

	@Query("select 1.0 * count(a) / (select count(b) from Aircraft b) from Aircraft a where a.status = 0")
	Double ratioOfActiveAircrafts();

	@Query("select 1.0 * count(a) / (select count(b) from Aircraft b) from Aircraft a where a.status != 0")
	Double ratioOfNonActiveAircrafts();

	@Query("select 1.0 * count(r) / (select count(s) from Review s) from Review r where r.score > 5.0")
	Double ratioOfReviewsWithScoreAboveFive();

	@Query("select count(r) from Review r where r.moment between :start and :end")
	Long numberOfReviewsPostedInWeek(Date start, Date end);

}
