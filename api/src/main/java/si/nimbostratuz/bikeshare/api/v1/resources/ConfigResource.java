package si.nimbostratuz.bikeshare.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import si.nimbostratuz.bikeshare.services.configuration.BikeshareConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Log
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
