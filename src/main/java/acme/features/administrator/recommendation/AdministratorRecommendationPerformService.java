
package acme.features.administrator.recommendation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Recommendation;

@GuiService
public class AdministratorRecommendationPerformService extends AbstractGuiService<Administrator, Recommendation> {

	private final AdministratorRecommendationRepository repository;


	@Autowired
	public AdministratorRecommendationPerformService(final AdministratorRecommendationRepository repository) {
		this.repository = repository;
	}

	// AbstractGuiService interface -------------------------------------------

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Recommendation recommendation;

		recommendation = new Recommendation();

		super.getBuffer().addData(recommendation);
	}

	@Override
	public void bind(final Recommendation recommendation) {
		super.bindObject(recommendation, "city", "country");
	}

	@Override
	public void validate(final Recommendation recommendation) {
		// Intentionally left empty: no extra validation needed for Passenger in this context.
	}

	@Override
	public void perform(final Recommendation recommendation) {
		String city;
		String country;
		Recommendation current;

		city = super.getRequest().getData("city", String.class);
		country = super.getRequest().getData("country", String.class);
		current = this.computeRecommendation(city, country);
		if (current == null) {
			recommendation.setName(null);
			recommendation.setPlaceId(null);
			recommendation.setWebsite(null);
		} else {
			recommendation.setName(current.getName());
			recommendation.setPlaceId(current.getPlaceId());
			recommendation.setWebsite(current.getWebsite());
		}
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset;

		dataset = super.unbindObject(recommendation, "city", "country", "name", "placeId", "website");

		super.getResponse().addData(dataset);
	}

	// Ancillary methods ------------------------------------------------------

	protected Recommendation computeRecommendation(final String city, final String country) {
		assert city != null;
		assert country != null;

		Recommendation result;

		if (SpringHelper.isRunningOn("production"))
			result = this.computeLiveRecommendation(city, country);
		else
			result = this.computeMockedRecommendation(city, country);

		return result;
	}

	protected Recommendation computeMockedRecommendation(final String city, final String country) {
		assert city != null;
		assert country != null;

		Recommendation result = new Recommendation();

		result.setCity(city);
		result.setCountry(country);
		result.setName("Name");
		result.setPlaceId("Place ID");
		result.setWebsite("http://a.b");

		return result;
	}

	protected Recommendation computeLiveRecommendation(final String city, final String country) {
		assert city != null;
		assert country != null;

		try {
			String placeId = this.findPlaceId(city, country);
			if (placeId == null) {
				super.state(false, "*", "administration.recommendation.form.label.not-found");
				return null;
			}

			Recommendation recommendation = this.fetchRecommendation(placeId);
			if (recommendation == null)
				super.state(false, "*", "administration.recommendation.form.label.not-found");

			MomentHelper.sleep(2000);
			return recommendation;

		} catch (Throwable oops) {
			// Justified: Catching Throwable because the external API may behave unpredictably
			super.state(false, "*", "administration.recommendation.form.label.api-error");
			return null;
		}
	}

	private String findPlaceId(final String city, final String country) throws IOException {
		String apiKey = "b482bc544d994d7b909c1f3ddc806229";
		String url = "https://api.geoapify.com/v1/geocode/search?text={0}&format=json&apiKey={1}";

		String body = this.makeApiCall(url, city, apiKey);
		JsonNode results = new ObjectMapper().readTree(body).get("results");

		if (results != null && results.isArray())
			for (JsonNode r : results) {
				String rCity = r.path("city").asText();
				String rCountry = r.path("country").asText();

				if (city.equalsIgnoreCase(rCity) && country.equalsIgnoreCase(rCountry))
					return r.path("place_id").asText();
			}

		return null;
	}

	private Recommendation fetchRecommendation(final String placeId) throws IOException {
		String apiKey = "b482bc544d994d7b909c1f3ddc806229";
		String url = "https://api.geoapify.com/v2/places?apiKey={0}&categories=tourism&filter=place:{1}";

		String body = this.makeApiCall(url, apiKey, placeId);
		JsonNode features = new ObjectMapper().readTree(body).get("features");

		if (features != null && features.isArray())
			for (JsonNode f : features) {
				JsonNode properties = f.get("properties");
				String recommendationId = properties.path("place_id").asText("None");

				Recommendation inDB = this.repository.getRecommendationByPlaceId(recommendationId);
				if (inDB == null) {
					Recommendation recommendation = new Recommendation();
					recommendation.setCity(properties.path("city").asText(null));
					recommendation.setCountry(properties.path("country").asText(null));
					recommendation.setName(properties.path("name").asText(null));
					recommendation.setPlaceId(recommendationId);
					recommendation.setWebsite(properties.path("website").asText(null));

					this.repository.save(recommendation);
					return recommendation;
				}
			}

		return null;
	}

	private String makeApiCall(final String url, final Object... uriVariables) {
		RestTemplate api = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> parameters = new HttpEntity<>("parameters", headers);
		ResponseEntity<String> response = api.exchange(url, HttpMethod.GET, parameters, String.class, uriVariables);
		return response.getBody();
	}

}
