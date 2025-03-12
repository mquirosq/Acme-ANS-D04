
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.BannedPassenger;

@Validator
public class BannedPassengerValidator extends AbstractValidator<ValidBannedPassenger, BannedPassenger> {

	@Override
	protected void initialise(final ValidBannedPassenger annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final BannedPassenger bannedPassenger, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (bannedPassenger == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean liftedAtAfterIssuedAt;
			if (bannedPassenger.getLiftedAt() != null) {
				liftedAtAfterIssuedAt = bannedPassenger.getIssuedAt().before(bannedPassenger.getLiftedAt());
				super.state(context, liftedAtAfterIssuedAt, "liftedAt", "acme.validation.customer.liftedAt.message");
			}
			boolean issuedAtAfterBirthDate = bannedPassenger.getIssuedAt().after(bannedPassenger.getDateOfBirth());
			super.state(context, issuedAtAfterBirthDate, "issuedAt", "acme.validation.customer.issuedAt.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
