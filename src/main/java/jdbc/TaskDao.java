package jdbc;

import models.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TaskDao {
    long insertTask(Task task) throws SQLException;

    Optional<Task> findById(Long id) throws SQLException;

    int update(Task task) throws SQLException;

    public boolean deleteTaskById(Long task_id) throws SQLException;
}
