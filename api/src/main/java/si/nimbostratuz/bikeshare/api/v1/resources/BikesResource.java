package si.nimbostratuz.bikeshare.api.v1.resources;

import si.nimbostratuz.bikeshare.services.BicyclesBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("bicycles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BikesResource {

    @Inject
    private BicyclesBean bicyclesBean;

    @GET
    public Response getBicycles() {

        return Response.ok(bicyclesBean.getAll()).build();
    }

    @GET
    @Path("{id}")
    public Response getBicycles(@PathParam("id") Integer id) {

        return Response.ok(bicyclesBean.getBicycle(id)).build();
    }
}
