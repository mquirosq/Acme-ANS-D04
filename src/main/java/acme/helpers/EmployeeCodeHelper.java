
package acme.helpers;

import acme.client.components.principals.DefaultUserIdentity;

public abstract class EmployeeCodeHelper {

	protected EmployeeCodeHelper() {

	}

	public static boolean checkFormatIsCorrect(final String employeeCode, final DefaultUserIdentity dui) {
		return dui.getName().charAt(0) == employeeCode.charAt(0) && dui.getSurname().charAt(0) == employeeCode.charAt(1);
	}

	public static boolean checkUniqueness(final Object inputEmployee, final Object obtainedEmployee) {
		return obtainedEmployee == null || obtainedEmployee.equals(inputEmployee);
	}

}
