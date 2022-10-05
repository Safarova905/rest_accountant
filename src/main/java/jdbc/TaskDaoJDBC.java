package jdbc;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import models.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
public class TaskDaoJDBC implements TaskDao{
    private final SessionManager sessionManager;

    public TaskDaoJDBC(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public long insertTask(Task task) throws SQLException {
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_TASK.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, task.getUserId());
            pst.setString(2, task.getTitle());
            pst.setString(3, task.getDescription());
            pst.setString(4, task.getDate());
            pst.setInt(5, task.getHours());
            pst.setInt(6, task.getMinutes());

            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                long id = rs.getLong(1);
                sessionManager.commitSession();

                return id;
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }
    }

    @Override
    public Optional<Task> findById(Long id) throws SQLException {
        Task task = null;
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_TASK_BY_ID.QUERY)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    task = new Task();
                    task.setId(Integer.parseInt(rs.getString("id")));
                    task.setUserId(rs.getInt("userId"));
                    task.setTitle(rs.getString("title"));
                    task.setDescription(rs.getString("description"));
                    task.setDate(rs.getString("date"));
                    task.setHours(rs.getInt("hours"));
                    task.setMinutes(rs.getInt("minutes"));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return Optional.ofNullable(task);    }

    @Override
    public int update(Task task) throws SQLException {
        int rowsUpdated = 0;
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_TASK_BY_ID.QUERY)) {
            pst.setInt(1, task.getId());
            pst.setInt(2, task.getUserId());
            pst.setString(3, task.getTitle());
            pst.setString(4, task.getDescription());
            pst.setString(5, task.getDate());
            pst.setInt(6, task.getHours());
            pst.setInt(7, task.getMinutes());

            rowsUpdated = pst.executeUpdate();
            sessionManager.commitSession();

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }
        return rowsUpdated;    }


    @Override
    public boolean deleteTaskById(Long task_id) throws SQLException {
        boolean result;

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.DELETE_TASK_BY_ID.QUERY)) {
            pst.setLong(1, task_id);

            int rows = pst.executeUpdate();
            result = rows == 1;

            sessionManager.commitSession();


        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return result;
    }

    enum SQLTask {
        INSERT_TASK("INSERT INTO tasks (id, userid, title, description, date, hours, minutes) VALUES ((?), (?), (?), (?), (?), (?), (?)"),
        GET_TASK_BY_ID("select tasks.id, tasks.userId, tasks.title, tasks.description, tasks.date, tasks.hours, tasks.minutes" +
                " WHERE tasks.id = (?)"),
        DELETE_TASK_BY_ID("DELETE from tasks where id=(?)"),
        UPDATE_TASK_BY_ID("UPDATE tasks set userId=(?), title=(?), description=(?), date=(?), hours=(?), minutes=(?) where id=(?)");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
