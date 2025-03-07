
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

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
		else {
			Boolean matches;
			String initials;
			String[] surnames;
			DefaultUserIdentity identity;

			identity = assistanceAgent.getIdentity();

			surnames = identity.getSurname().split(" ");

			initials = "";
			initials += identity.getName().trim().charAt(0);

			for (String surname : surnames)
				initials += surname.trim().charAt(0);

			matches = assistanceAgent.getEmployeeCode().trim().startsWith(initials);
			super.state(context, !matches, "employeeCode", "acme.validation.assistanceAgent.employeeCode.message");
		}
		result = !super.hasErrors(context);
		return result;
	}
}
