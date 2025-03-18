
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.CustomerRepository;
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
		else {
			boolean initialsInIdentifier;

			char nameInitial = customer.getIdentity().getName().trim().charAt(0);
			String[] surnames = customer.getIdentity().getSurname().trim().split(" ");
			char surnameInitial = surnames[0].trim().charAt(0);

			initialsInIdentifier = customer.getIdentifier().charAt(0) == nameInitial && customer.getIdentifier().charAt(1) == surnameInitial;

			super.state(context, initialsInIdentifier, "identifier", "acme.validation.customer.identifier.message");

			if (customer.getIdentifier() != null) {
				Customer existingCustomer = this.repository.getCustomerByIdentifier(customer.getIdentifier());
				boolean uniqueIdentifier = existingCustomer == null || existingCustomer.equals(customer);
				super.state(context, uniqueIdentifier, "identifier", "acme.validation.customer.identifier.unique.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}
}
