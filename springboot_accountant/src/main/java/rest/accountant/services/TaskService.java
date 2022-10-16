package rest.accountant.services;

import org.springframework.stereotype.Service;
import rest.accountant.entities.Task;

public interface TaskService {
    Iterable<Task> listAllProducts();

    Task getTaskById(Integer id);

    Task saveTask(Task task);

    void deleteTask(Integer id);
}
