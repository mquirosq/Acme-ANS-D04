
package acme.features.customer.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Recommendation;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationListService extends AbstractGuiService<Customer, Recommendation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerRecommendationRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Recommendation> recommendations = new ArrayList<>();

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		List<String> destinations = this.repository.findFlightsOfCustomer(customerId).stream().map(f -> f.getDestinationCity() + "," + f.getDestinationCountry()).toList();
		for (String destination : destinations) {
			String city = destination.split(",")[0].trim();
			String country = destination.split(",")[1].trim();
			List<Recommendation> placeRecommend = this.repository.getRecommendationsForCityAndCountry(city, country);
			recommendations.addAll(placeRecommend);
		}
		super.getBuffer().addData(recommendations);
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset;

		dataset = super.unbindObject(recommendation, "city", "country", "name", "website");

		super.getResponse().addData(dataset);
	}
}
