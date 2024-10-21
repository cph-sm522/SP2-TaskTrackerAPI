package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    //EKSEMPEL: private final DogRoute dogRoutes = new DogRoute(); // Tilføjer dog ruterne

    public EndpointGroup getApiRoutes() {
        return () -> {
            //EKSEMPEL: path("api/v1/dogs", dogRoutes.getDogRoutes());  // Binder hunderuterne til path "/dog"
        };
    }

}
