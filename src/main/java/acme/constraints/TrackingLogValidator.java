
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
				super.state(context, !trackingLog.getLastUpdateMoment().before(trackingLog.getCreationMoment()), "*", "acme.validation.trackingLog.lastUpdateMoment.message");
			}
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
				super.state(context, !(trackingLog.getIsPublished() && !trackingLog.getClaim().getIsPublished()), "*", "acme.validation.trackingLog.isPublished.message");
			}
			{
				List<TrackingLog> trackingLogs;

				trackingLogs = this.repository.findAllByClaimId(trackingLog.getClaim().getId());

				for (Integer i = 0; i < trackingLogs.size(); i++) {
					TrackingLog t1;
					TrackingLog t2;

					t1 = trackingLogs.get(i);

					if (trackingLogs.size() > i + 1) {
						t2 = trackingLogs.get(i + 1);
						super.state(context, t1.getResolutionPercentage() < t2.getResolutionPercentage(), "*", "acme.validation.trackingLog.resolutionPercentage.message");
					}
				}
			}
		}
		result = !super.hasErrors(context);
		return result;
	}
}
