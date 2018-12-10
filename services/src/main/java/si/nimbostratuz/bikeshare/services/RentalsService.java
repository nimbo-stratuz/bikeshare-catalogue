package si.nimbostratuz.bikeshare.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.glassfish.jersey.client.ClientProperties;
import si.nimbostratuz.bikeshare.models.dtos.RentalDTO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.Optional;

@Log
@ApplicationScoped
public class RentalsService {

    private static Logger log = LogManager.getLogger(RentalsService.class.getSimpleName());

    @SuppressWarnings({"CdiInjectionPointsInspection", "OptionalUsedAsFieldOrParameterType"})
    @Inject
    @DiscoverService("bikeshare-rental")
    private Optional<WebTarget> rentalWebTarget;

    @PostConstruct
    public void init() {
        this.rentalWebTarget = rentalWebTarget.map(
                webTarget -> webTarget.property(ClientProperties.CONNECT_TIMEOUT, 500)
                                      .property(ClientProperties.READ_TIMEOUT, 500));
    }

    @Metered
    public Optional<List<RentalDTO>> getLastRentalsForBicycle(Integer bicycleId, Integer limit) {

        log.debug("rentalWebTarget: {}", rentalWebTarget.map(wt -> wt.getUri().toString())
                                                        .orElse("None"));

        if (rentalWebTarget.isPresent()) {
            try {
                return Optional.of(rentalWebTarget.get().path("v1")
                                                  .path("rentals")
                                                  .queryParam("where", "bicycleId:EQ:" + bicycleId)
                                                  .queryParam("limit", limit)
                                                  .queryParam("order", "rentStart DESC")
                                                  .request().get(new GenericType<List<RentalDTO>>() {}));
            } catch (ProcessingException e) {
                log.error("getLastRentalsForBicycle", e);
            }
        } else {
            log.warn("bikeshare-rentals cannot be discovered");
        }

        return Optional.empty();
    }
}
