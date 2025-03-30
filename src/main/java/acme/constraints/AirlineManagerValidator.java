
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.helpers.EmployeeCodeHelper;
import acme.realms.AirlineManager;
import acme.realms.AirlineManagerRepository;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	@Autowired
	private AirlineManagerRepository repository;


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
		else if (manager.getIdentifierNumber() != null) {
			String identifierNumber = manager.getIdentifierNumber();
			DefaultUserIdentity identity = manager.getIdentity();

			boolean isCorrectlyFormatted = EmployeeCodeHelper.checkFormatIsCorrect(identifierNumber, identity);
			super.state(context, isCorrectlyFormatted, "identifier", "acme.validation.airlineManager.identifierFormat.message");

			AirlineManager obtainedManager = this.repository.findByIdentifierNumber(identifierNumber);

			boolean isUnique = EmployeeCodeHelper.checkUniqueness(manager, obtainedManager);
			super.state(context, isUnique, "identifier", "acme.validation.airlineManager.identifierUniqueness.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
