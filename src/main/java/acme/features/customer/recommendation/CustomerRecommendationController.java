
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

	@Autowired
	private CustomerRecommendationListService	listService;

	@Autowired
	private CustomerRecommendationShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
