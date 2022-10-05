package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdbc.TaskDaoJDBC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.Task;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class RestApiDeleteHandlerImpl implements RestApiHandler{
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskDaoJDBC taskDao;

    @Override
    public Optional<String> handleRestRequest(String requestPath) throws SQLException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest request) throws SQLException, IOException, JsonProcessingException {
        long generated_id = 0;
        if (requestPath.matches("^/tasks/\\d+$")) {
            final Integer userId = request.getIntHeader("userId");
            final String title = request.getParameter("title");
            final String description = request.getParameter("description");
            final String date = request.getParameter("date");
            final Integer hours = request.getIntHeader("hours");
            final Integer minutes = request.getIntHeader("minute");

            final Task task = new Task();
            task.setUserId(userId);
            task.setTitle(title);
            task.setDescription(description);
            task.setDate(date);
            task.setHours(hours);
            task.setMinutes(minutes);

            return taskDao.update(task);

        }

        return generated_id;
    }
}
