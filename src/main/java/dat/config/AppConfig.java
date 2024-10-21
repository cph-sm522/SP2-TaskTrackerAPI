package dat.config;

import dat.routes.Routes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

public class AppConfig {

    private static final Routes routes = new Routes();

    // Konfiguration af Javalin-serveren
    private static void configuration(JavalinConfig config) {
        config.router.apiBuilder(routes.getApiRoutes()); // Tilføjer ruterne fra Routes-klassen
    }

    // Start serveren
    public static void startServer() {
        var app = Javalin.create(AppConfig::configuration);
        app.start(7007); // Starter Javalin-serveren på port 7007
    }
}