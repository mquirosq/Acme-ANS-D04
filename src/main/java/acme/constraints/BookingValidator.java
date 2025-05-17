
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.Booking;
import acme.entities.BookingRepository;
import acme.helpers.ValidatorHelper;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private BookingRepository repository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (booking == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			if (booking.getLocatorCode() != null) {
				Booking existingBooking = this.repository.getSameLocatorCode(booking.getLocatorCode());
				boolean uniqueLocatorCode = ValidatorHelper.checkUniqueness(booking, existingBooking);
				super.state(context, uniqueLocatorCode, "locatorCode", "acme.validation.booking.locatorCode.message");
			}
			if (!booking.isDraftMode()) {
				boolean hasCreditCardNibble;
				boolean hasSomePassengers;

				hasCreditCardNibble = booking.getLastCardNibble() != null && !booking.getLastCardNibble().isBlank();
				super.state(context, hasCreditCardNibble, "lastCreditCardNibble", "acme.validation.booking.lastCreditCardNibble.message");

				hasSomePassengers = this.repository.getNumberOfPassengers(booking.getId()).compareTo(0L) > 0;
				super.state(context, hasSomePassengers, "*", "acme.validation.booking.passengers.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
