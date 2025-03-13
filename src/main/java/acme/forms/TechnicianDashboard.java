
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.recordStatus;
import acme.entities.Aircraft;
import acme.entities.MaintenanceRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Map<recordStatus, Long>		recordsByStatus;

	MaintenanceRecord			recordNearestInspection;

	List<Aircraft>				topAircraftsByTasks;

	Long						avgMaintenanceCosts;

	Long						minMaintenanceCost;

	Long						maxMaintenanceCost;

	Double						standardDevMaintenanceCost;

	Long						avgDurationTasks;

	Long						minDurationTask;

	Long						maxDurationTask;

	Double						standardDevDurationTask;
}
