
package acme.features.any.review;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.MomentHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.Review;

@Repository
public interface AnyReviewRepository extends AbstractRepository {

	@Query("select r from Review r where r.id = :id")
	Review findReviewById(int id);

	@Query("select r from Review r")
	Collection<Review> findAllReviews();

	@Query("select r from Review r where r.moment > :date")
	List<Review> findReviewsByDate(Date date);

	default List<Review> findReviewsFromLastYear() {
		List<Review> result;
		Date date;
		List<Review> list;

		date = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);
		list = this.findReviewsByDate(date);
		result = list.isEmpty() ? null : list;

		return result;
	}
}
