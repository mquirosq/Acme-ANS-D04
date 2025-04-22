
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.CustomerRepository;
import acme.helpers.ValidatorHelper;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private CustomerRepository repository;


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (customer.getIdentifier() != null) {
			boolean initialsInIdentifier;
			initialsInIdentifier = ValidatorHelper.checkFormatIsCorrect(customer.getIdentifier(), customer.getIdentity());
			super.state(context, initialsInIdentifier, "identifier", "acme.validation.customer.identifier.message");

			Customer existingCustomer = this.repository.getCustomerByIdentifier(customer.getIdentifier());
			boolean uniqueIdentifier = ValidatorHelper.checkUniqueness(customer, existingCustomer);
			super.state(context, uniqueIdentifier, "identifier", "acme.validation.customer.identifier.unique.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
