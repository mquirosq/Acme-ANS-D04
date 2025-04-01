
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.datatypes.ClaimStatus;
import acme.entities.Claim;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (claim == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (claim.getIsPublished() && (claim.getStatus().equals(ClaimStatus.NO_STATUS) || claim.getStatus().equals(ClaimStatus.PENDING)))
			super.state(context, false, "isPublished", "acme.validation.claim.isPublished.message");
		result = !super.hasErrors(context);
		return result;
	}
}
