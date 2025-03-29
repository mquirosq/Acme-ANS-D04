
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

	@Autowired
	private AnyReviewRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
