
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.Airport;
import acme.entities.AirportRepository;
import acme.helpers.ValidatorHelper;

@Validator
public class AirportValidator extends AbstractValidator<ValidAirport, Airport> {

	@Autowired
	private AirportRepository repository;


	@Override
	protected void initialise(final ValidAirport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (airport == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (airport.getIATACode() != null) {
			Airport existingAirport = this.repository.getByIATACode(airport.getIATACode());
			boolean hasUniqueIATA = ValidatorHelper.checkUniqueness(airport, existingAirport);

			super.state(context, hasUniqueIATA, "IATACode", "acme.validation.airport.notUniqueIATA.message");
		}
		result = !super.hasErrors(context);
		return result;
	}
}
