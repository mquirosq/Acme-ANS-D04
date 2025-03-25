
package acme.helpers;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.helpers.StringHelper;

public abstract class EmployeeCodeHelper {

	protected EmployeeCodeHelper() {

	}

	public static boolean checkFormatIsCorrect(final String employeeCode, final DefaultUserIdentity dui) {
		return !StringHelper.isBlank(dui.getName()) && !StringHelper.isBlank(dui.getSurname()) && !StringHelper.isBlank(employeeCode) && employeeCode.trim().length() >= 2 && dui.getName().charAt(0) == employeeCode.charAt(0)
			&& dui.getSurname().charAt(0) == employeeCode.charAt(1);
	}

	public static boolean checkUniqueness(final Object inputEmployee, final Object obtainedEmployee) {
		return obtainedEmployee == null || obtainedEmployee.equals(inputEmployee);
	}

}
