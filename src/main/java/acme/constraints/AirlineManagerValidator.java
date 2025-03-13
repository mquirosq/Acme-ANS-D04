
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AirlineManager;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	@Override
	protected void initialise(final ValidAirlineManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager manager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean initialsInIdentifier;

			char nameInitial = manager.getIdentity().getName().trim().charAt(0);
			char surnameInitial = manager.getIdentity().getSurname().trim().charAt(0);

			initialsInIdentifier = manager.getIdentifierNumber().charAt(0) == nameInitial && manager.getIdentifierNumber().charAt(1) == surnameInitial;

			super.state(context, initialsInIdentifier, "identifier", "acme.validation.airlineManager.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
