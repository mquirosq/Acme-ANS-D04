
package acme.features.administrator.recommendation;

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

	@Autowired
	AdministratorRecommendationRepository repository;

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
		;
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

		Recommendation result = null;
		RestTemplate api;
		HttpHeaders headers;
		HttpEntity<String> parameters;
		ResponseEntity<String> responsePlace;
		String recordPlace;
		String placeId = "None";
		ResponseEntity<String> responseRecommend;
		String apiKey;
		boolean exception = false;
		boolean notFound = false;

		try {
			apiKey = "b482bc544d994d7b909c1f3ddc806229";
			headers = new HttpHeaders();
			parameters = new HttpEntity<String>("parameters", headers);
			api = new RestTemplate();
			responsePlace = api.exchange( //	
				"https://api.geoapify.com/v1/geocode/search?text={0}&format=json&apiKey={1}", //
				HttpMethod.GET, //
				parameters, //
				String.class, city, //
				apiKey //
			);
			assert responsePlace.getBody() != null;
			recordPlace = responsePlace.getBody();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(recordPlace);
			JsonNode results = root.get("results");

			if (results.isArray())
				for (JsonNode r : results) {
					String rCity = r.path("city").asText();
					String rCountry = r.path("country").asText();

					if (city.equalsIgnoreCase(rCity) && country.equalsIgnoreCase(rCountry)) {
						placeId = r.path("place_id").asText();
						break;
					}
				}
			if (placeId.equals("None")) {
				result = null;
				notFound = true;
			} else {
				headers = new HttpHeaders();
				parameters = new HttpEntity<String>("parameters", headers);
				api = new RestTemplate();
				responseRecommend = api.exchange( //	
					"https://api.geoapify.com/v2/places?apiKey={0}&categories=tourism&filter=place:{1}", //
					HttpMethod.GET, //
					parameters, //
					String.class, apiKey, //
					placeId //
				);

				assert responseRecommend.getBody() != null;

				mapper = new ObjectMapper();
				root = mapper.readTree(responseRecommend.getBody());
				results = root.get("features");

				if (results.isArray()) {
					for (JsonNode f : results) {
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

							result = recommendation;
							this.repository.save(result);
							break;
						}
					}
					if (result == null)
						notFound = true;
				} else {
					result = null;
					notFound = true;
				}
			}
			MomentHelper.sleep(2000);
		} catch (final Throwable oops) {
			exception = true;
			result = null;
		}
		super.state(!exception, "*", "administration.recommendation.form.label.api-error");
		super.state(!notFound, "*", "administration.recommendation.form.label.not-found");
		return result;
	}

}
