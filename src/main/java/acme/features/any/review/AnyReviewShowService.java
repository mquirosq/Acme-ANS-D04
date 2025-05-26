
package acme.features.any.review;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Review;

@GuiService
public class AnyReviewShowService extends AbstractGuiService<Any, Review> {

	// Internal state ---------------------------------------------------------

	private final AnyReviewRepository repository;


	@Autowired
	public AnyReviewShowService(final AnyReviewRepository repository) {
		this.repository = repository;
	}

	// AbstractGuiService interface -------------------------------------------

	@Override
	public void authorise() {
		boolean authorised;
		String reviewIdInput;
		int reviewId;
		Review review;

		try {
			reviewIdInput = super.getRequest().getData("id", String.class);
			reviewId = Integer.parseInt(reviewIdInput);
			review = this.repository.findReviewById(reviewId);
			authorised = review != null;
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}

		super.getResponse().setAuthorised(authorised);

	}

	@Override
	public void load() {
		Review review;
		int id;

		id = super.getRequest().getData("id", int.class);
		review = this.repository.findReviewById(id);

		super.getBuffer().addData(review);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "alias", "moment", "subject", "text", "score", "recommended");
		dataset.put("readonly", true);

		super.getResponse().addData(dataset);
	}

}
