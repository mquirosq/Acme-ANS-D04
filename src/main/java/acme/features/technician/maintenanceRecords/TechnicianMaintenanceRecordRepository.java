
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Aircraft;
import acme.entities.MaintenanceRecord;
import acme.entities.Task;
import acme.entities.TaskRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordbyId(int id);

	@Query("select m from MaintenanceRecord m where m.technician.id = :technicianId")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTechnicianId(int technicianId);

	@Query("select t.task from TaskRecord t where t.record.id = :maintenanceId")
	Collection<Task> findTasksByMaintenanceRecordId(int maintenanceId);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select tr from TaskRecord tr where tr.record.id = :id")
	Collection<TaskRecord> getTaskRecordsByMaintenanceRecordId(int id);
}
