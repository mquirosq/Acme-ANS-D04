
package acme.features.any.review;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Review;
import acme.helpers.InternationalisationHelper;

@GuiService
public class AnyReviewListService extends AbstractGuiService<Any, Review> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyReviewRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Review> reviews;
		reviews = this.repository.findReviewsFromLastYear();

		super.getBuffer().addData(reviews);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "alias", "subject", "text", "score");
		super.addPayload(dataset, review, "moment");
		dataset.put("payload", dataset.get("payload") + "|" + InternationalisationHelper.internationalizeBoolean(review.getRecommended()));

		super.getResponse().addData(dataset);
	}
}
