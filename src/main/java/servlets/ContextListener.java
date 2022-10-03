package servlets;

import models.Task;
import util.TaskUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class ContextListener implements ServletContextListener {

    private Map<Integer, Task> tasks;
    private AtomicInteger idCounter;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        tasks = new ConcurrentHashMap<>();
        idCounter = new AtomicInteger(4);
        servletContext.setAttribute("tasks", tasks);
        servletContext.setAttribute("idCounter", idCounter);

        List<Task> taskList = TaskUtils.generateTasks();
        taskList.forEach(task -> this.tasks.put(task.getId(), task));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        tasks = null;
    }
}
