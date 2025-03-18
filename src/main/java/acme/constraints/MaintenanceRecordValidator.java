
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.MaintenanceRecord;

@Validator
public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenanceRecord, MaintenanceRecord> {

	@Override
	protected void initialise(final ValidMaintenanceRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord maintenanceRecord, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (maintenanceRecord == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Boolean check;
			Date today;
			Date dueInspection;
			Date maintenanceDate;

			today = MomentHelper.getCurrentMoment();
			dueInspection = maintenanceRecord.getInspectionDue();
			maintenanceDate = maintenanceRecord.getMaintenanceDate();

			check = dueInspection.after(today) && maintenanceDate.before(dueInspection);
			super.state(context, check, "dueInspection", "acme.validation.maintenanceRecord.dueInspection.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
