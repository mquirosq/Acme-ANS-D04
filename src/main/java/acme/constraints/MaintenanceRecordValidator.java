
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
			Date dueDate;

			today = MomentHelper.getCurrentMoment();
			dueDate = maintenanceRecord.getMaintenanceDate();

			check = dueDate.after(today);
			super.state(context, !check, "license", "acme.validation.maintenanceRecord.license.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
