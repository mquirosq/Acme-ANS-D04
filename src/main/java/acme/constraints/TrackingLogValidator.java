
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
				super.state(context, !trackingLog.getLastUpdateMoment().before(trackingLog.getCreationMoment()), "lastUpdateMoment", "acme.validation.trackingLog.lastUpdateMoment.message");
			}
			{
				if (trackingLog.getResolutionPercentage() == 100.0)
					super.state(context, !trackingLog.getStatus().equals(ClaimStatus.PENDING), "status", "acme.validation.trackingLog.notPendingStatus.message");
				else
					super.state(context, trackingLog.getStatus().equals(ClaimStatus.PENDING), "status", "acme.validation.trackingLog.pendingStatus.message");
			}
			{
				if (!trackingLog.getStatus().equals(ClaimStatus.PENDING))
					super.state(context, !StringHelper.isBlank(trackingLog.getResolution()), "resolution", "acme.validation.trackingLog.resolution.message");
			}
			{
				super.state(context, !(trackingLog.getIsPublished() && !trackingLog.getClaim().getIsPublished()), "isPublished", "acme.validation.trackingLog.isPublished.message");
			}
			{
				if (trackingLog.getStatus().equals(ClaimStatus.RECLAIMED)) {
					boolean existsCompletedLog, isUnique;
					List<TrackingLog> trackingLogs, reclaimedLogs;

					trackingLogs = this.repository.findAllByClaimId(trackingLog.getClaim().getId());
					existsCompletedLog = trackingLogs.stream().anyMatch(t -> t.getResolutionPercentage() == 100.0 && t.getIsPublished());

					super.state(context, existsCompletedLog, "status", "acme.validation.trackingLog.reclaimed.noCompletedLog.message");

					reclaimedLogs = this.repository.findAllByClaimIdAndStatus(trackingLog.getClaim().getId(), ClaimStatus.RECLAIMED);
					isUnique = reclaimedLogs.isEmpty() || reclaimedLogs.size() == 1 && reclaimedLogs.get(0).equals(trackingLog);

					super.state(context, isUnique, "status", "acme.validation.trackingLog.reclaimed.notUnique.message");
				}
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

						if (!t2.getStatus().equals(ClaimStatus.RECLAIMED))
							super.state(context, t1.getResolutionPercentage() < t2.getResolutionPercentage(), "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");
					}
				}
			}
		}
		result = !super.hasErrors(context);
		return result;
	}
}
