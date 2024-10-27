package dat.routes;

import dat.config.HibernateConfig;
import dat.controllers.UserController;
import dat.daos.UserDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserRoutes {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("tasktracker");
    private final UserDAO userDAO = new UserDAO(emf);
    private final UserController userController = new UserController(userDAO);

    public EndpointGroup getUserRoutes() {
        return () -> {
            path("/", () -> {
                get("/{id}", userController::getUserById);
                put("/{id}", userController::updateUser);
                delete("/{id}", userController::deleteUser);
            });
        };
    }
}
