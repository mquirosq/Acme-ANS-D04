
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.Booking;
import acme.entities.BookingRepository;

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
		else if (booking.getLocatorCode() != null) {
			Booking existingBooking = this.repository.getSameLocatorCode(booking.getLocatorCode());
			boolean uniqueLocatorCode = existingBooking == null || existingBooking.equals(booking);
			super.state(context, uniqueLocatorCode, "locatorCode", "acme.validation.booking.locatorCode.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
