
package acme.features.technician.taskRecords;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.MaintenanceRecord;
import acme.entities.Task;
import acme.entities.TaskRecord;

@Repository
public interface TechnicianTaskRecordRepository extends AbstractRepository {

	@Query("select tr.record from TaskRecord tr where tr.id = :id")
	MaintenanceRecord findMaintenanceRecordOfTaskRecordById(int id);

	@Query("select tr from TaskRecord tr where tr.id = :id")
	TaskRecord findTaskRecordById(int id);

	@Query("select tr.task from TaskRecord tr where tr.record.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);

	@Query("select tr from TaskRecord tr where tr.record.id = :id")
	Collection<TaskRecord> findTaskRecordsByMaintenanceRecordId(int id);

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select tr.task from TaskRecord tr where tr.id = :id")
	Collection<Task> findAllTasksByTaskRecordId(int id);

	@Query("select tr.record from TaskRecord tr where tr.id = :id")
	Collection<MaintenanceRecord> findAllMaintenanceRecordsByTaskRecordId(int id);

	@Query("select tr.task from TaskRecord tr where tr.id = :id")
	Task findTaskByTaskRecordId(int id);

	@Query("select t from Task t where t not in (select tr.task from TaskRecord tr where tr.record.id = :id) and (t.isDraft = false or t.technician.id = :technicianId)")
	Collection<Task> findNewTasksforMaintenanceRecord(int id, int technicianId);

	@Query("select tr.task from TaskRecord tr where tr.task.id = :taskId and tr.maintenanceRecord.id = :maintenanceRecordId")
	Task findValidTaskByIdAndMaintenanceRecord(int taskId, int maintenanceRecordId);

	@Query("select t from Task where t.id = :taskId and (t.technician.id = :technicianId or t.isDraft = false)")
	Task findTechniciansTaskByIds(int taskId, int technicianId);

	@Query("select t from Task where t.id = :id")
	Task findTaskById(int id);

}
