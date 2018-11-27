package si.nimbostratuz.bikeshare.api.v1.resources;

import com.kumuluz.ee.common.runtime.EeRuntime;
import si.nimbostratuz.bikeshare.api.v1.dtos.HealthDTO;
import si.nimbostratuz.bikeshare.api.v1.dtos.LoadDTO;
import si.nimbostratuz.bikeshare.services.configuration.BikeshareConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
@Path("demo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {
    private Logger log = Logger.getLogger(DemoResource.class.getName());

    @Inject
    private BikeshareConfig bikeshareConfig;

    @GET
    @Path("instanceid")
    public Response getInstanceId() {

        String instanceId =
                "{\"instanceId\" : \"" + EeRuntime.getInstance().getInstanceId() + "\"}";

        return Response.ok(instanceId).build();
    }

    @POST
    @Path("healthy")
    public Response setHealth(HealthDTO health) {
        bikeshareConfig.setHealthy(health.getHealthy());
        log.info("Setting health to " + health.getHealthy());
        return Response.ok().build();
    }

    @POST
    @Path("load")
    public Response loadOrder(LoadDTO loadDto) {

        for (int i = 1; i <= loadDto.getN(); i++) {
            fibonacci(i);
        }

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("info")
    public Response info() {

        JsonObject json = Json.createObjectBuilder()
                              .add("clani", Json.createArrayBuilder()
                                                .add("mf6422")
                                                .add("ap6932"))
                              .add("opis_projekta", "Najin projekt implementira aplikacijo za deljenje in izposojo " +
                                                    "koles z uporabo pametnih ključavnic.")
                              .add("mikrostoritve", Json.createArrayBuilder()
                                                        .add("http://159.122.186.143:31337/v1/bicycles")
                                                        .add("http://159.122.186.143:30420/v1/rentals"))
                              .add("github", Json.createArrayBuilder()
                                                 .add("https://github.com/nimbo-stratuz/bikeshare-catalogue")
                                                 .add("https://github.com/nimbo-stratuz/bikeshare-rental"))
                              .add("travis", Json.createArrayBuilder()
                                                 .add("https://travis-ci.org/nimbo-stratuz/bikeshare-catalogue")
                                                 .add("https://travis-ci.org/nimbo-stratuz/bikeshare-rental"))
                              .add("dockerhub", Json.createArrayBuilder()
                                                    .add("https://hub.docker.com/r/nimbostratuz/bikeshare-catalogue")
                                                    .add("https://hub.docker.com/r/nimbostratuz/bikeshare-rental"))
                              .build();


        return Response.ok(json.toString()).build();
    }

    private long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
