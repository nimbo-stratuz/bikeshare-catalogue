package si.nimbostratuz.bikeshare.api.v1.resources;

import si.nimbostratuz.bikeshare.models.entities.Bicycle;
import si.nimbostratuz.bikeshare.services.BicyclesBean;
import si.nimbostratuz.bikeshare.services.configuration.BikeshareConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("config")
@Produces(MediaType.TEXT_PLAIN)
public class ConfigResource {

    @Inject
    private BikeshareConfig bikeshareConfig;

    @GET
    public Response get() {

        return Response.ok(bikeshareConfig.toString()).build();
    }
}
