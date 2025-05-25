
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.Recommendation;
import acme.entities.RecommendationRepository;
import acme.helpers.ValidatorHelper;

@Validator
public class RecommendationValidator extends AbstractValidator<ValidRecommendation, Recommendation> {

	private final RecommendationRepository repository;


	@Autowired
	public RecommendationValidator(final RecommendationRepository repository) {
		this.repository = repository;
	}

	@Override
	protected void initialise(final ValidRecommendation annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Recommendation recommendation, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (recommendation == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (recommendation.getPlaceId() != null) {
			boolean uniquePlaceId;
			Recommendation recommendationObtained;

			recommendationObtained = this.repository.getSamePlaceId(recommendation.getPlaceId());
			uniquePlaceId = ValidatorHelper.checkUniqueness(recommendation, recommendationObtained);

			super.state(context, uniquePlaceId, "placeId", "acme.validation.recommendation.uniquePlaceId.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
