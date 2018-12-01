package si.nimbostratuz.bikeshare.api.v1;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService
@ApplicationPath("v1")
@CrossOrigin
@DeclareRoles({"user", "admin"})
public class CatalogueApplication extends Application {
}
