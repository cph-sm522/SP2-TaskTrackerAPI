package dat.routes;

import dat.config.HibernateConfig;
import dat.controllers.TaskController;
import dat.daos.TaskDAO;
import dat.daos.UserDAO;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TaskRoutes {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("tasktracker");
    private TaskDAO taskDAO = new TaskDAO(emf);
    private UserDAO userDAO = new UserDAO(emf);
    private TaskController taskController = new TaskController(taskDAO, userDAO);

    public EndpointGroup getTaskRoutes(){
        return () -> {
            get("/", ctx -> taskController.getAllTasks(ctx), Role.ANYONE);
            get("/{id}", ctx -> taskController.getAllTasksFromUser(ctx), Role.ANYONE);
            post("/", ctx-> taskController.createTask(ctx), Role.ANYONE);
            put("/{id}", ctx -> taskController.updateTask(ctx), Role.ANYONE);
            delete("/{id}", ctx -> taskController.deleteTask(ctx), Role.ANYONE);
            delete("/user/{id}", ctx -> taskController.deleteAllTasksFromUser(ctx), Role.ANYONE);
        };
    }
}