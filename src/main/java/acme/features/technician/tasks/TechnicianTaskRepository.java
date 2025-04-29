
package acme.features.technician.tasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Task;
import acme.entities.TaskRecord;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.technician.id = :technicianId")
	Collection<Task> findAllTasksByTechnicianId(int technicianId);

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select tr from TaskRecord tr where tr.task.id = :id")
	Collection<TaskRecord> getTaskRecordsByTaskId(int id);

}
