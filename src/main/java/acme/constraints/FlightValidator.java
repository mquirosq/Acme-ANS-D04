
package acme.constraints;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.Flight;
import acme.entities.FlightLeg;
import acme.entities.FlightLegRepository;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	@Autowired
	private FlightLegRepository repository;


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flight == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (flight.getDraftMode() != null && !flight.getDraftMode()) {
			boolean isNotOverlapping = true;
			boolean allLegsPublished = true;
			boolean atLeastOneLeg = true;

			List<FlightLeg> legsOfFlight = this.repository.getLegsOfFlight(flight.getId());

			if (legsOfFlight.size() <= 0)
				atLeastOneLeg = false;

			super.state(context, atLeastOneLeg, "legs", "acme.validation.flight.missingLegs.message");

			for (Integer i = 0; i < legsOfFlight.size() - 1; i++) {
				{
					Date scheduledArrivalFirst = legsOfFlight.get(i).getScheduledArrival();
					Date scheduledDepartureSecond = legsOfFlight.get(i + 1).getScheduledDeparture();
					if (MomentHelper.isAfterOrEqual(scheduledArrivalFirst, scheduledDepartureSecond))
						isNotOverlapping = false;
				}
				{
					if (legsOfFlight.get(i).getDraftMode())
						allLegsPublished = false;
				}
			}

			super.state(context, isNotOverlapping, "legs", "acme.validation.flight.overlappingDates.message");
			super.state(context, allLegsPublished, "legs", "acme.validation.flight.notPublishedLeg.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
