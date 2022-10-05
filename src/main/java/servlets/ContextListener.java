package servlets;

import jdbc.*;
import service.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;


@WebListener
public class ContextListener implements ServletContextListener {
    private TaskDaoJDBC taskDaoJDBC;
    private RestApiHandler restApiGetHandlerService;
    private RestApiHandler restApiPostHandlerService;
    private RestApiHandler restApiPutHandlerService;
    private RestApiHandler restApiDeleteHandlerService;


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        DataSource dataSource = DataSourceHikariPostgres.getHikariDataSource();
        SessionManager sessionManager = new SessionManagerJDBC(dataSource);
        this.taskDaoJDBC = new TaskDaoJDBC(sessionManager);

        restApiGetHandlerService = new RestApiGetHandlerImpl(taskDaoJDBC);
        restApiPostHandlerService = new RestApiPostHandlerImpl(taskDaoJDBC);
        restApiPutHandlerService = new RestApiPutHandlerImpl(taskDaoJDBC);
        restApiDeleteHandlerService = new RestApiDeleteHandlerImpl(taskDaoJDBC);

        servletContext.setAttribute("taskDAO", taskDaoJDBC);
        servletContext.setAttribute("restApiGetHandlerService", restApiGetHandlerService);
        servletContext.setAttribute("restApiPostHandlerService", restApiPostHandlerService);
        servletContext.setAttribute("restApiPutHandlerService", restApiPutHandlerService);
        servletContext.setAttribute("restApiDeleteHandlerService", restApiDeleteHandlerService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
