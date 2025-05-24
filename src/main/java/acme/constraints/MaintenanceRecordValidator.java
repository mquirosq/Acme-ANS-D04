
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
		else if (maintenanceRecord.getMaintenanceDate() != null && maintenanceRecord.getInspectionDue() != null && maintenanceRecord.getCost() != null) {
			Boolean check, check2;
			Date today;
			Date inspectionDue;
			Date maintenanceDate;

			today = MomentHelper.getCurrentMoment();
			inspectionDue = maintenanceRecord.getInspectionDue();
			maintenanceDate = maintenanceRecord.getMaintenanceDate();

			check = MomentHelper.isAfter(inspectionDue, today);
			super.state(context, check, "inspectionDue", "acme.validation.maintenanceRecord.dueInspection.message");

			check2 = MomentHelper.isBefore(maintenanceDate, inspectionDue);
			super.state(context, check, "maintenanceDate", "acme.validation.maintenanceRecord.maintenanceDate.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
