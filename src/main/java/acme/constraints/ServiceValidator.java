
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.Service;
import acme.entities.ServiceRepository;

public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	@Autowired
	private ServiceRepository repository;


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
			Boolean unique;
			String promotionCode = service.getPromotionCode();
			String currentDate = MomentHelper.getCurrentMoment().toString().strip();

			check = promotionCode.endsWith(currentDate.substring(currentDate.length() - 2));
			super.state(context, check, "promotionCode", "acme.validation.service.promotioncode.message");
			unique = this.repository.getEqual(service.getPromotionCode()) == 1;
			super.state(context, unique, "promotionCode", "acme.validation.service.promotioncodeuniqueness.message");
		}

		result = !super.hasErrors(context);
		return result;
	}

}
