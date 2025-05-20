
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.helpers.ValidatorHelper;
import acme.realms.FlightCrewMember;
import acme.realms.FlightCrewMemberRepository;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Autowired
	private FlightCrewMemberRepository repository;


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
		else if (flightCrewMember.getEmployeeCode() != null) {
			Boolean check;
			Boolean uniqueness;
			DefaultUserIdentity dui = flightCrewMember.getIdentity();
			String employeeCode = flightCrewMember.getEmployeeCode();

			check = ValidatorHelper.checkFormatIsCorrect(employeeCode, dui);

			super.state(context, check, "employeeCode", "acme.validation.flightcrewmember.employeecode.message");

			FlightCrewMember fcm = this.repository.findByEmployeeCode(employeeCode);

			uniqueness = ValidatorHelper.checkUniqueness(flightCrewMember, fcm);

			super.state(context, uniqueness, "employeeCode", "acme.validation.flightcrewmember.uniqueemployeecode.message");

		}

		result = !super.hasErrors(context);
		return result;
	}
}
