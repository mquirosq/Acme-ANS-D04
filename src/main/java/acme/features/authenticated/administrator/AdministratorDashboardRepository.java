
package acme.features.authenticated.administrator;

import java.util.Date;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.datatypes.AirlineType;
import acme.datatypes.AirportScope;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("select a.scope, count(a) from Airport a group by a")
	Map<AirportScope, Long> totalNumberOfAirportsByOperationalScope();

	@Query("select a.type, count(a) from Airline a group by a.type")
	Map<AirlineType, Long> numberOfAirlinesByType();

	@Query("select 1.0 * count(a) / (select count(b) from Airline b) from Airline a where a.email != null and a.phoneNumber != null")
	Double ratioOfAirlinesWithEmailAddressAndPhoneNumber();

	@Query("select 1.0 * count(a) / (select count(b) from Aircraft b) from Aircraft a where a.status = 0")
	Double ratioOfActiveAircrafts();

	@Query("select 1.0 * count(a) / (select count(b) from Aircraft b) from Aircraft a where a.status != 0")
	Double ratioOfNonActiveAircrafts();

	@Query("select 1.0 * count(r) / (select count(s) from Review s) from Review r where r.score > 5.0")
	Double ratioOfReviewsWithScoreAboveFive();

	@Query("select count(r) from Review r where r.moment between :start and :end")
	Long numberOfReviewsPostedInLastTenWeeks(Date start, Date end);

	@Query("select avg(select count(r) from Review r where r.moment = s.moment) from Review s where s.moment between :start and :end")
	Double averageNumberOfReviewsPostedInLastTenWeeks(Date start, Date end);

	@Query("select min(select count(r) from Review r where r.moment = s.moment) from Review s where s.moment between :start and :end")
	Long minimumNumberOfReviewsPostedInLastTenWeeks(Date start, Date end);

	@Query("select max(select count(r) from Review r where r.moment = s.moment) from Review s where s.moment between :start and :end")
	Long maximumNumberOfReviewsPostedInLastTenWeeks(Date start, Date end);

	// @Query("select stddev(select count(r) from Review r where r.moment = s.moment) from Review s where s.moment between :start and :end")
	Double standardDeviationOfNumberOfReviewsPostedInLastTenWeeks(Date start, Date end);

}
