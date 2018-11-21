package si.nimbostratuz.bikeshare.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService("bikeshare-catalogue")
@ApplicationPath("v1")
public class CatalogueApplication extends Application {
}
