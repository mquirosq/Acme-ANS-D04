
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
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
			{
				boolean departureIsBeforeArrival = leg.getScheduledDeparture().compareTo(leg.getScheduledArrival()) < 0;

				super.state(context, departureIsBeforeArrival, "dates", "acme.validation.flightLeg.scheduledDates.message");
			}
			{
				boolean isNotOverlapping = true;

				List<FlightLeg> legsOfFlight = this.repository.getLegsOfFlight(leg.getParentFlight().getId());

				for (Integer i = 0; i < legsOfFlight.size() - 1; i++)
					if (legsOfFlight.get(i).getScheduledArrival().compareTo(legsOfFlight.get(i + 1).getScheduledDeparture()) >= 0) {
						isNotOverlapping = false;
						break;
					}
				super.state(context, isNotOverlapping, "dates", "acme.validation.flight.overlappingDates.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}
}
