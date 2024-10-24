package dat.routes;

import dat.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final TaskRoutes taskRoutes = new TaskRoutes(); // Adds task routes
    private final UserRoutes userRoutes = new UserRoutes();
    private final SecurityRoutes securityRoutes = new SecurityRoutes();

    public EndpointGroup getApiRoutes() {
        return () -> {
            // Public routes: No authentication required
            path("/auth", securityRoutes.getSecurityRoutes()); // For login and registration

            // Protected routes: Requires authentication
            path("/api", () -> {
                path("/tasks", taskRoutes.getTaskRoutes());  // Task-related routes (CRUD)
                path("/users", userRoutes.getUserRoutes());  // User-related routes (CRUD)
                path("/protected", securityRoutes.getSecuredRoutes()); // Secured routes (user and admin demos)
            });
        };
    }
}
