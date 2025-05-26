
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
			boolean contained = false;
			String[] acceptedCurrencies = config.getAcceptedCurrencies().split(",");

			Set<String> uniqueCurrencies = new HashSet<>();

			for (String currency : acceptedCurrencies) {
				String trimmed = currency.trim();
				if (!trimmed.isEmpty() && !uniqueCurrencies.add(trimmed))
					repeated = true;
			}

			if (config.getSystemCurrency() != null && config.getSystemCurrency().length() == 3)
				contained = uniqueCurrencies.contains(config.getSystemCurrency().trim());

			super.state(context, !repeated, "acceptedCurrencies", "acme.validation.systemConfiguration.acceptedCurrencies.repeated");
			super.state(context, contained, "systemCurrency", "acme.validation.systemConfiguration.acceptedCurrencies.contained");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
