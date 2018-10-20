package si.nimbostratuz.bikeshare.api.v1.resources;

import si.nimbostratuz.bikeshare.services.BicyclesBean;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("bikes")
public class BikesResource {

    @Inject
    private BicyclesBean bicyclesBean;

    @GET
    public Response getBicycles() {

        return Response.ok(bicyclesBean.getAll()).build();
    }
}
