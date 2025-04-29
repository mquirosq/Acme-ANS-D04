
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.helpers.ValidatorHelper;
import acme.realms.AssistanceAgent;
import acme.realms.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Autowired
	private AssistanceAgentRepository repository;


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (assistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (assistanceAgent.getEmployeeCode() != null) {
			String employeeCode;
			DefaultUserIdentity identity;
			Boolean isCorrectlyFormatted, isUnique;
			AssistanceAgent obtainedAssistanceAgent;

			identity = assistanceAgent.getIdentity();
			employeeCode = assistanceAgent.getEmployeeCode();
			obtainedAssistanceAgent = this.repository.findByEmployeeCode(employeeCode);

			isCorrectlyFormatted = ValidatorHelper.checkFormatIsCorrect(employeeCode, identity);
			super.state(context, isCorrectlyFormatted, "employeeCode", "acme.validation.assistanceAgent.employeeCodeFormat.message");

			isUnique = ValidatorHelper.checkUniqueness(assistanceAgent, obtainedAssistanceAgent);
			super.state(context, isUnique, "employeeCode", "acme.validation.assistanceAgent.employeeCodeUniqueness.message");
		}
		result = !super.hasErrors(context);
		return result;
	}
}
