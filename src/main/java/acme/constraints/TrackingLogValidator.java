
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.datatypes.ClaimStatus;
import acme.entities.TrackingLog;
import acme.entities.TrackingLogRepository;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private TrackingLogRepository repository;


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				if (trackingLog.getResolutionPercentage() == 100.0)
					super.state(context, !trackingLog.getStatus().equals(ClaimStatus.PENDING), "*", "acme.validation.trackingLog.notPendingStatus.message");
				else
					super.state(context, trackingLog.getStatus().equals(ClaimStatus.PENDING), "*", "acme.validation.trackingLog.pendingStatus.message");
			}
			{
				if (!trackingLog.getStatus().equals(ClaimStatus.PENDING))
					super.state(context, !StringHelper.isBlank(trackingLog.getResolution()), "*", "acme.validation.trackingLog.resolution.message");
			}
			{
				List<TrackingLog> trackingLogs;

				trackingLogs = this.repository.findAllByClaimIdWithDifferentIdBefore(trackingLog.getClaim().getId(), trackingLog.getId(), trackingLog.getCreationMoment());

				if (!trackingLogs.isEmpty())
					super.state(context, trackingLogs.get(0).getResolutionPercentage() < trackingLog.getResolutionPercentage(), "*", "acme.validation.trackingLog.resolutionPercentage.message");
			}
		}
		result = !super.hasErrors(context);
		return result;
	}
}
