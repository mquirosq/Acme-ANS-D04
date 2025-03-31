
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.helpers.EmployeeCodeHelper;
import acme.realms.Technician;
import acme.realms.TechnicianRepository;

@Validator
public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Autowired
	private TechnicianRepository repository;


	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (technician == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (technician.getLicense() != null) {
			String license;
			DefaultUserIdentity identity;
			Boolean isCorrectlyFormatted, isUnique;
			Technician obtainedTechnician;

			identity = technician.getIdentity();
			license = technician.getLicense();
			obtainedTechnician = this.repository.findByLicense(license);

			isCorrectlyFormatted = EmployeeCodeHelper.checkFormatIsCorrect(license, identity);
			super.state(context, isCorrectlyFormatted, "license", "acme.validation.technician.licenseCodeFormat.message");

			isUnique = EmployeeCodeHelper.checkUniqueness(technician, obtainedTechnician);
			super.state(context, isUnique, "license", "acme.validation.technician.licenseUniqueness.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
