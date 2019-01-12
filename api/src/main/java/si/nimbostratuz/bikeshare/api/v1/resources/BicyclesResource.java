package si.nimbostratuz.bikeshare.api.v1.resources;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.security.annotations.Secure;
import com.kumuluz.ee.logs.cdi.Log;
import si.nimbostratuz.bikeshare.models.entities.Bicycle;
import si.nimbostratuz.bikeshare.services.BicyclesBean;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Log
@ApplicationScoped
@Secure
@Path("bicycles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BicyclesResource {

    private static Logger log = LogManager.getLogger(BicyclesResource.class.getName());

    @Inject
    private BicyclesBean bicyclesBean;

    @GET
    @PermitAll
    public Response getBicycles(@QueryParam("latitude") Double latitude,
                                @QueryParam("longitude") Double longitude) {

        log.info(latitude + " | " + longitude);

        if (latitude != null && longitude != null) {
            return Response.ok(bicyclesBean.getClosest(latitude, longitude)).build();
        }

        return Response.ok(bicyclesBean.getAll()).build();
    }

    @GET
    @Path("{id}")
    public Response getBicycles(@PathParam("id") Integer id) {

        return Response.ok(bicyclesBean.get(id)).build();
    }

    @POST
    @RolesAllowed("user")
    public Response createBicycle(Bicycle bicycle) {

        return Response.ok(bicyclesBean.create(bicycle)).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("user")
    public Response updateBicycle(@PathParam("id") Integer id, Bicycle bicycle) {

        return Response.ok(bicyclesBean.update(id, bicycle)).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("user")
    public Response deleteBicycle(@PathParam("id") Integer id) {

        bicyclesBean.delete(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
