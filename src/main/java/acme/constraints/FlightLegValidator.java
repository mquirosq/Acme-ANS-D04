
package acme.constraints;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.FlightLeg;
import acme.entities.FlightLegRepository;

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
				boolean departureIsBeforeArrival = leg.getScheduledDeparture().compareTo(leg.getScheduledArrival()) < 0;

				super.state(context, departureIsBeforeArrival, "dates", "acme.validation.flightLeg.scheduledDates.message");
			}
			if (leg.getParentFlight() != null) {
				boolean isNotOverlapping = true;

				List<FlightLeg> legsOfFlight = this.repository.getLegsOfFlight(leg.getParentFlight().getId());

				for (Integer i = 0; i < legsOfFlight.size() - 1; i++) {
					Date scheduledArrivalFirst = legsOfFlight.get(i).getScheduledArrival();
					Date scheduledDepartureSecond = legsOfFlight.get(i + 1).getScheduledDeparture();
					if (MomentHelper.isAfterOrEqual(scheduledArrivalFirst, scheduledDepartureSecond))
						isNotOverlapping = false;
				}
				super.state(context, isNotOverlapping, "dates", "acme.validation.flight.overlappingDates.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
