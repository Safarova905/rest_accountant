package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import models.Task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@WebServlet(urlPatterns = "/tasks/*")
public class RestTaskServlet extends HttpServlet {

    private Map<Integer, Task> tasks;
    private AtomicInteger idCounter;

    @Override
    public void init() {
        final Object tasks = getServletContext().getAttribute("tasks");
        if (!(tasks instanceof ConcurrentHashMap)) {
            throw new IllegalStateException(" - ");
        }
        else {
            this.tasks = (ConcurrentHashMap<Integer, Task>) tasks;
        }

        idCounter = (AtomicInteger) getServletContext().getAttribute("idCounter");

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        String[] parts = pathInfo.split("/");
        String param1 = parts[1];

        int taskId = Integer.parseInt(param1);

        request.setCharacterEncoding("UTF-8");

        final Task task = tasks.get(taskId);
        final String jsonTask = new ObjectMapper().writeValueAsString(task);

        response.setContentType("application/json, charset-UTF-8");
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        out.write(jsonTask);
    }
//create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        final String title = request.getParameter("title");
        final String description = request.getParameter("description");

        final Task task = new Task();
        final int id = this.idCounter.getAndIncrement();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);

        tasks.put(id, task);

        response.setStatus(201);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();
        String parts[] = pathInfo.split("/");
        String param1 = parts[1];

        tasks.remove(Integer.parseInt(param1));

        response.setStatus(202);
    }

//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
//        String pathInfo = request.getPathInfo();
//
//    }



}
