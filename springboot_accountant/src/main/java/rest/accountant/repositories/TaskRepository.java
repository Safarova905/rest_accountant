package rest.accountant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rest.accountant.entities.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
