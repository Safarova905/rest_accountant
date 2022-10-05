package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdbc.TaskDaoJDBC;
import lombok.extern.slf4j.Slf4j;
import models.Task;
import service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


@Slf4j
@WebServlet(urlPatterns = "/tasks/*")
public class RestTaskServlet extends HttpServlet {

    private static final String TASK_ADD_ERROR = "Произошла ошибка, задача не добавлена !\n";
    private static final String TASK_UPDATE_ERROR = "Произошла ошибка, задача не обновлена\n";
    private static final String TASK_CREATED_SUCCESS_JSON = "{ \"task_id\" : \"%d\" }";
    private TaskDaoJDBC taskDao;
    private RestApiHandler restApiGetHandler;
    private RestApiHandler restApiPostHandler;
    private RestApiHandler restApiPutHandler;
    private RestApiHandler restApiDeleteHandler;

    @Override
    public void init() throws ServletException {
        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        final Object restApiGetHandlerService = getServletContext().getAttribute("restApiGetHandlerService");
        final Object restApiPostHandlerService = getServletContext().getAttribute("restApiPostHandlerService");
        final Object restApiPutHandlerService = getServletContext().getAttribute("restApiPutHandlerService");
        final Object restApiDeleteHandlerService = getServletContext().getAttribute("restApiDeleteHandlerService");

        this.taskDao = (TaskDaoJDBC) taskDAO;
        this.restApiGetHandler = (RestApiGetHandlerImpl) restApiGetHandlerService;
        this.restApiPostHandler = (RestApiPostHandlerImpl) restApiPostHandlerService;
        this.restApiPutHandler = (RestApiPutHandlerImpl) restApiPutHandlerService;
        this.restApiDeleteHandler = (RestApiDeleteHandlerImpl) restApiDeleteHandlerService;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Пришел запрос {} на URI: {}", request.getMethod(), request.getRequestURI());
        String pathInfo = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/HTML; charset=UTF-8");

        try {
            String user_response = restApiGetHandler.handleRestRequest(pathInfo).orElseThrow(SQLException::new);
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(200);
            PrintWriter out = response.getWriter();
            out.write(user_response);

        } catch (SQLException e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.write("Не найдено задачи с таким ID");
            response.setStatus(404);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Пришел запрос {} на URI: {}", request.getMethod(), request.getRequestURI());
        String pathInfo = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");


        try {
            long generated_id = restApiPostHandler.handleRestRequest(pathInfo, request);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(String.format(TASK_CREATED_SUCCESS_JSON,generated_id));
            response.setStatus(201);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("\"user_id\" нарушает ограничение NOT NULL")) {
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(TASK_ADD_ERROR);
                response.setStatus(404);
                return;
            }

            response.setContentType("text/HTML; charset=UTF-8");
            response.getWriter().write(TASK_ADD_ERROR + e.getMessage());
            response.setStatus(400);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Пришел запрос {} на URI: {}", request.getMethod(), request.getRequestURI());
        String pathInfo = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/HTML; charset=UTF-8");

        try {
            restApiPutHandler.handleRestRequest(pathInfo, request);
            response.setStatus(200);
        } catch (SQLException e) {
            response.setStatus(400);
            response.getWriter().write(TASK_UPDATE_ERROR + e.getMessage());
        }
    }

}
