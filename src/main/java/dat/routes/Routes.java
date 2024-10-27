package dat.routes;

import dat.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final TaskRoutes taskRoutes = new TaskRoutes();
    private final UserRoutes userRoutes = new UserRoutes();
    private final SecurityRoutes securityRoutes = new SecurityRoutes();

    public EndpointGroup getApiRoutes() {
        return () -> {

            path("/api/auth", securityRoutes.getSecurityRoutes());
            path("/api", () -> {
                path("/tasks", taskRoutes.getTaskRoutes());
                path("/users", userRoutes.getUserRoutes());
            });
        };
    }
}
