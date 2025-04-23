
package acme.helpers;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.models.Request;
import acme.client.components.principals.DefaultUserIdentity;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.entities.Flight;
import acme.entities.FlightLeg;
import acme.entities.FlightLegRepository;

public abstract class ValidatorHelper {

	protected ValidatorHelper() {

	}

	public static boolean checkFormatIsCorrect(final String employeeCode, final DefaultUserIdentity dui) {
		return employeeCode != null && dui != null && !StringHelper.isBlank(dui.getName()) && !StringHelper.isBlank(dui.getSurname()) && !StringHelper.isBlank(employeeCode) && employeeCode.trim().length() >= 2
			&& dui.getName().charAt(0) == employeeCode.charAt(0) && dui.getSurname().charAt(0) == employeeCode.charAt(1);
	}

	public static boolean checkUniqueness(final Object inputEmployee, final Object obtainedEmployee) {
		return obtainedEmployee == null || obtainedEmployee.equals(inputEmployee);
	}

	public static List<Boolean> validatePublishedFlight(final Flight flight) {
		boolean isNotOverlapping = true;
		boolean allLegsPublished = true;
		boolean atLeastOneLeg = true;

		FlightLegRepository flightLegRepository = SpringHelper.getBean(FlightLegRepository.class);

		List<FlightLeg> legsOfFlight = flightLegRepository.getLegsOfFlight(flight.getId());

		if (legsOfFlight.size() <= 0)
			atLeastOneLeg = false;

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

		return List.of(atLeastOneLeg, isNotOverlapping, allLegsPublished);
	}

	public static <T extends AbstractEntity> boolean isValidEntityReference(final String field, final Request request, final Collection<T> legalValues) {
		boolean res = true;
		if (request.hasData(field)) {
			int dataId;
			String dataIdRaw = request.getData(field, String.class);
			try {
				dataId = Integer.parseInt(dataIdRaw);
			} catch (NumberFormatException e) {
				dataId = -1;
				res = false;
			}

			if (dataId != 0) {
				List<Integer> legalValuesIds = legalValues.stream().map(v -> v.getId()).toList();
				res &= legalValuesIds.contains(dataId);
			}
		}
		return res;
	}

}
