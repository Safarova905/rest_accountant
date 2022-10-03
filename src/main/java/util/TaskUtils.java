package util;

import models.Task;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskUtils {
    public static List<Task> generateTasks() {
        final Task task1 = new Task(1, "Write Hello World", "Create a simple program on Java", "03.10.2022", 3);
        final Task task2 = new Task(2, "Learn the theory", "The area is Spring", "02.03.2021", 2);
        final Task task3 = new Task(3, "Look for a bag", "Not enough the memory", "24.12.2021", 34);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        return tasks;
    }


    public static boolean idIsNumber(HttpServletRequest request) {
        final String id = request.getParameter("id");
        return id != null &&
                (id.length() > 0) &&
                id.matches("^\\d*$");
    }

    public static boolean requestIsValid(HttpServletRequest request) {
        final String title = request.getParameter("title");
        final String description = request.getParameter("description");

        return title != null && title.length() > 0 &&
                description != null && description.length() > 0;
    }


    public static boolean idIsInvalid(final String id, Map<Integer, Task> repo) {
        return !(id != null &&
                id.matches("^\\d*$") &&
                repo.get(Integer.parseInt(id)) != null);
    }
}
