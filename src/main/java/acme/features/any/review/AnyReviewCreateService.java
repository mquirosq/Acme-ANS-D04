
package acme.features.any.review;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Review;

@GuiService
public class AnyReviewCreateService extends AbstractGuiService<Any, Review> {

	@Autowired
	private AnyReviewRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Review review;

		review = new Review();
		super.getBuffer().addData(review);
	}

	@Override
	public void bind(final Review review) {
		super.bindObject(review, "alias", "moment", "subject", "text", "score", "recommended");
	}

	@Override
	public void validate(final Review review) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);

		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Review review) {
		this.repository.save(review);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;
		dataset = super.unbindObject(review, "alias", "moment", "subject", "text", "score", "recommended");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
