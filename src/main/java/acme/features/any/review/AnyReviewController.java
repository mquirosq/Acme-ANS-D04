
package acme.features.any.review;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Review;

@GuiController
public class AnyReviewController extends AbstractGuiController<Any, Review> {

	// Internal state ---------------------------------------------------------

	private final AnyReviewListService		listService;

	private final AnyReviewShowService		showService;

	private final AnyReviewCreateService	createService;


	@Autowired
	public AnyReviewController(final AnyReviewListService listService, final AnyReviewShowService showService, final AnyReviewCreateService createService) {
		this.listService = listService;
		this.showService = showService;
		this.createService = createService;
	}

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
	}
}
