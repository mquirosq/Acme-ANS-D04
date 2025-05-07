
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.Flight;
import acme.helpers.ValidatorHelper;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flight == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (flight.getDraftMode() != null && !flight.getDraftMode()) {
			List<Boolean> validation = ValidatorHelper.validatePublishedFlight(flight);

			boolean atLeastOneLeg = validation.get(0);
			boolean isNotOverlapping = validation.get(1);
			boolean allLegsPublished = validation.get(2);

			super.state(context, atLeastOneLeg, "legs", "acme.validation.flight.missingLegs.message");
			super.state(context, isNotOverlapping, "legs", "acme.validation.flight.overlappingDates.message");
			super.state(context, allLegsPublished, "legs", "acme.validation.flight.notPublishedLeg.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
