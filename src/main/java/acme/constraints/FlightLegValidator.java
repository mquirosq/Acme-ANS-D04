
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.FlightLeg;
import acme.entities.FlightLegRepository;
import acme.helpers.ValidatorHelper;

@Validator
public class FlightLegValidator extends AbstractValidator<ValidFlightLeg, FlightLeg> {

	@Autowired
	private FlightLegRepository repository;


	@Override
	protected void initialise(final ValidFlightLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightLeg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			if (leg.getFlightNumber() != null && leg.getFlightNumber().trim().length() >= 3 && leg.getDeployedAircraft() != null) {
				boolean firstCharsFromIATA;

				String airlineIATA = leg.getDeployedAircraft().getAirline().getIATACode();
				firstCharsFromIATA = airlineIATA.equals(leg.getFlightNumber().substring(0, 3));

				super.state(context, firstCharsFromIATA, "flightNumber", "acme.validation.flightLeg.flightNumber.message");
			}
			if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
				boolean departureIsBeforeArrival = MomentHelper.isBefore(leg.getScheduledDeparture(), leg.getScheduledArrival());

				super.state(context, departureIsBeforeArrival, "scheduledDeparture", "acme.validation.flightLeg.scheduledDates.message");
				super.state(context, departureIsBeforeArrival, "scheduledArrival", "acme.validation.flightLeg.scheduledDates.message");
			}
			if (leg.getFlightNumber() != null) {
				FlightLeg existingLeg = this.repository.getByFlightNumber(leg.getFlightNumber());
				boolean uniqueFlightNumber = ValidatorHelper.checkUniqueness(leg, existingLeg);
				super.state(context, uniqueFlightNumber, "flightNumber", "acme.validation.flightLeg.flightNumberUnique.message");
			}
			if (leg.getArrivalAirport() != null && leg.getDepartureAirport() != null) {
				boolean departureIsNotArrival = !leg.getArrivalAirport().equals(leg.getDepartureAirport());
				super.state(context, departureIsNotArrival, "arrivalAirport", "acme.validation.flightLeg.sameAirports.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
