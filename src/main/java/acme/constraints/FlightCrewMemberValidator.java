
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.FlightCrewMember;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (flightCrewMember == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Boolean check;
			DefaultUserIdentity dui = flightCrewMember.getIdentity();
			String employeeCode = flightCrewMember.getEmployeeCode();

			check = dui.getName().charAt(0) == employeeCode.charAt(0) && dui.getSurname().charAt(0) == employeeCode.charAt(1);

			super.state(context, check, "promotionCode", "acme.validation.service.promotioncode.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
