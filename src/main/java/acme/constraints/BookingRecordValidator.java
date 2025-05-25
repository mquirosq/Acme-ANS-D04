
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.BookingRecord;
import acme.entities.BookingRecordRepository;
import acme.helpers.ValidatorHelper;

@Validator
public class BookingRecordValidator extends AbstractValidator<ValidBookingRecord, BookingRecord> {

	private final BookingRecordRepository repository;


	public BookingRecordValidator(final BookingRecordRepository repository) {
		this.repository = repository;
	}

	@Override
	protected void initialise(final ValidBookingRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final BookingRecord bookingRecord, final ConstraintValidatorContext context) {
		assert context != null; // Justification: this context should never be null here

		boolean result;

		if (bookingRecord == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (bookingRecord.getPassenger() != null && bookingRecord.getBooking() != null) {
			boolean uniquePassengerInBooking;
			BookingRecord bookingRecordObtained;

			bookingRecordObtained = this.repository.getSameBookingAndPassenger(bookingRecord.getPassenger(), bookingRecord.getBooking());
			uniquePassengerInBooking = ValidatorHelper.checkUniqueness(bookingRecord, bookingRecordObtained);

			super.state(context, uniquePassengerInBooking, "*", "acme.validation.bookingRecord.uniquePassengerInBooking.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
