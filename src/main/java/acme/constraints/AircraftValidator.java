
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.Aircraft;
import acme.entities.AircraftRepository;
import acme.helpers.ValidatorHelper;

@Validator
public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	final AircraftRepository repository;


	@Autowired
	public AircraftValidator(final AircraftRepository repository) {
		this.repository = repository;
	}

	@Override
	protected void initialise(final ValidAircraft annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (aircraft == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (aircraft.getRegistrationNumber() != null) {
			Boolean uniqueAircraft;
			Aircraft existingAircraft;

			existingAircraft = this.repository.findAircraftByRegistrationNumber(aircraft.getRegistrationNumber());
			uniqueAircraft = ValidatorHelper.checkUniqueness(aircraft, existingAircraft);

			super.state(context, uniqueAircraft, "registrationNumber", "javax.validation.aircraft.registrationNumber.message");
		}
		result = !super.hasErrors(context);
		return result;
	}
}
