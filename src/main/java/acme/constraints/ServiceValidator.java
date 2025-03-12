
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.Service;

public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (service.getPromotionCode() != null) {
			Boolean check;
			String promotionCode = service.getPromotionCode();
			String currentDate = MomentHelper.getCurrentMoment().toString().strip();

			check = promotionCode.endsWith(currentDate.substring(currentDate.length() - 2));

			super.state(context, check, "promotionCode", "acme.validation.service.promotioncode.message");
		}

		result = !super.hasErrors(context);
		return result;
	}

}
