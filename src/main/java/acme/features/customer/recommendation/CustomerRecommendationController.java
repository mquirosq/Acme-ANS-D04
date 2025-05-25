
package acme.features.customer.recommendation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Recommendation;
import acme.realms.Customer;

@GuiController
public class CustomerRecommendationController extends AbstractGuiController<Customer, Recommendation> {

	// Internal state ---------------------------------------------------------

	private final CustomerRecommendationListService	listService;

	private final CustomerRecommendationShowService	showService;


	@Autowired
	public CustomerRecommendationController(final CustomerRecommendationListService listService, final CustomerRecommendationShowService showService) {
		this.listService = listService;
		this.showService = showService;
	}

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
