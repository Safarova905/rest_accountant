package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdbc.TaskDaoJDBC;
import models.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


public class JsonTaskServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskDaoJDBC taskDao;

    @Override
    public void init() throws ServletException {

        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        this.taskDao = (TaskDaoJDBC) taskDAO;

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        final String id = request.getParameter("id");

        try {
            Task task = taskDao.findById(Long.parseLong(id)).orElseThrow(SQLException::new);
            final String jsonTask = objectMapper.writeValueAsString(task);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(jsonTask);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/HTML; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write("Не найдено задачи с таким ID");
        }


    }
}
