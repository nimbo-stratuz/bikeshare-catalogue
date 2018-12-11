package si.nimbostratuz.bikeshare.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.glassfish.jersey.client.ClientProperties;
import si.nimbostratuz.bikeshare.models.common.RequestId;
import si.nimbostratuz.bikeshare.models.dtos.RentalDTO;
import si.nimbostratuz.bikeshare.services.configuration.BikeshareConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.Optional;

@Log
@ApplicationScoped
public class RentalsService {

    private static final Logger log = LogManager.getLogger(RentalsService.class.getSimpleName());

    @SuppressWarnings({"CdiInjectionPointsInspection", "OptionalUsedAsFieldOrParameterType"})
    @Inject
    @DiscoverService("bikeshare-rental")
    private Optional<WebTarget> rentalWebTarget;

    @Inject
    private BikeshareConfig config;

    @Inject
    private RequestId requestId;


    @Metered
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "getLastRentalsForBicycleFallback")
    public Optional<List<RentalDTO>> getLastRentalsForBicycle(Integer bicycleId, Integer limit) {

        log.info("Using main method for {}.getLastRentalsForBicycle", RentalsService.class.getSimpleName());

        log.debug("rentalWebTarget: {}", rentalWebTarget.map(wt -> wt.getUri().toString())
                                                        .orElse("None"));

        if (rentalWebTarget.isPresent()) {
            return Optional.of(rentalWebTarget.get().path("v1")
                                              .path("rentals")
                                              .queryParam("where", "bicycleId:EQ:" + bicycleId)
                                              .queryParam("limit", limit)
                                              .queryParam("order", "rentStart DESC")
                                              .request()
                                              .header("X-Request-ID", requestId.get())
                                              .property(ClientProperties.CONNECT_TIMEOUT, config.getRentalsServiceTimeout())
                                              .property(ClientProperties.READ_TIMEOUT, config.getRentalsServiceTimeout())
                                              .get(new GenericType<List<RentalDTO>>() {}));
        } else {
            log.warn("bikeshare-rentals cannot be discovered");
        }

        return Optional.empty();
    }

    public Optional<List<RentalDTO>> getLastRentalsForBicycleFallback(Integer bicycleId, Integer limit) {
        log.info("Using fallback for {}.getLastRentalsForBicycle", RentalsService.class.getSimpleName());
        return Optional.empty();
    }
}
