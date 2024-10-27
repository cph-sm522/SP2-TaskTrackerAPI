package dat.security.routes;

import com.fasterxml.jackson.databind.ObjectMapper;

import dat.utils.Utils;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SecurityRoutes {
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static SecurityController securityController = SecurityController.getInstance();

    public EndpointGroup getSecurityRoutes() {
        return () -> {
                get("/test", ctx -> ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from Open")), Role.ANYONE);
                post("/login", securityController.login(), Role.USER);
                post("/register", securityController.register(), Role.ANYONE);
                post("/user/addrole", securityController.addRole(), Role.ADMIN);
            };
    }
}
