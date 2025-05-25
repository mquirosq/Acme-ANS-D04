
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
public class CustomerRecommendationShowService extends AbstractGuiService<Customer, Recommendation> {

	// Internal state ---------------------------------------------------------

	private final CustomerRecommendationRepository repository;


	@Autowired
	public CustomerRecommendationShowService(final CustomerRecommendationRepository repository) {
		this.repository = repository;
	}

	// AbstractGuiService interface -------------------------------------------

	@Override
	public void authorise() {
		Boolean authorised;
		String rawId;
		int recommendationId;
		Recommendation recommendation;

		try {
			rawId = super.getRequest().getData("id", String.class);
			recommendationId = Integer.parseInt(rawId);
			recommendation = this.repository.findRecommendationById(recommendationId);

			Collection<Recommendation> recommendations = new ArrayList<>();

			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			List<String> destinations = this.repository.findFlightsOfCustomer(customerId).stream().map(f -> f.getDestinationCity() + "," + f.getDestinationCountry()).toList();
			for (String destination : destinations) {
				String city = destination.split(",")[0].trim();
				String country = destination.split(",")[1].trim();
				List<Recommendation> placeRecommend = this.repository.getRecommendationsForCityAndCountry(city, country);
				recommendations.addAll(placeRecommend);
			}

			authorised = recommendation != null && recommendations.contains(recommendation);
		} catch (NumberFormatException | AssertionError e) {
			authorised = false;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Integer recommendationId = super.getRequest().getData("id", Integer.class);
		Recommendation recommendation = this.repository.findRecommendationById(recommendationId);

		super.getBuffer().addData(recommendation);
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset;

		dataset = super.unbindObject(recommendation, "city", "country", "name", "website");

		super.getResponse().addData(dataset);
	}
}
