
package acme.constraints;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.SystemConfiguration;

public class SystemConfigurationValidator extends AbstractValidator<ValidSystemConfiguration, SystemConfiguration> {

	@Override
	protected void initialise(final ValidSystemConfiguration annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final SystemConfiguration config, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (config == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (config.getAcceptedCurrencies() != null) {
			boolean repeated = false;
			String[] acceptedCurrencies = config.getAcceptedCurrencies().split(",");

			Set<String> uniqueCurrencies = new HashSet<String>();

			for (String currency : acceptedCurrencies) {
				String trimmed = currency.trim();
				if (!trimmed.isEmpty() && !uniqueCurrencies.add(trimmed)) {
					repeated = true;
					break;
				}
			}

			super.state(context, !repeated, "acceptedCurrencies", "acme.validation.systemConfiguration.acceptedCurrencies.repeated");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
