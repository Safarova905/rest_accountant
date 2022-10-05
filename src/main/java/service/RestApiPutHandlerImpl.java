package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdbc.TaskDaoJDBC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.Task;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class RestApiPutHandlerImpl implements RestApiHandler{
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskDaoJDBC taskDao;

    @Override
    public Optional<String> handleRestRequest(String requestPath) throws SQLException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest request) throws SQLException, IOException, JsonProcessingException {
        long updatedRows = 0;
        if (requestPath.matches("^/tasks/\\d+$")) {
            String[] parts = requestPath.split("/");
            String taskIdParam = parts[2];
            int task_id = Integer.parseInt(taskIdParam);
            String bodyParams = request.getReader().lines().collect(Collectors.joining());

            Task task = objectMapper.readValue(bodyParams, Task.class);
            task.setId(task_id);

            updatedRows = taskDao.update(task);

        }
        return updatedRows;
    }
}
