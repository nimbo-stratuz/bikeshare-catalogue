package si.nimbostratuz.bikeshare.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.glassfish.jersey.client.ClientProperties;
import si.nimbostratuz.bikeshare.models.common.RequestId;
import si.nimbostratuz.bikeshare.models.dtos.RentalDTO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.time.temporal.ChronoUnit;
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

    @Inject
    private RequestId requestId;

    @PostConstruct
    public void init() {
        this.rentalWebTarget = rentalWebTarget.map(
                webTarget -> webTarget.property(ClientProperties.CONNECT_TIMEOUT, 500)
                                      .property(ClientProperties.READ_TIMEOUT, 500));
    }

    @Metered
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Timeout(value = 500L, unit = ChronoUnit.MILLIS)
    @Fallback(fallbackMethod = "getLastRentalsForBicycleFallback")
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
                                                  .request()
                                                  .header("X-Request-ID", requestId.get())
                                                  .get(new GenericType<List<RentalDTO>>() {}));
            } catch (ProcessingException e) {
                log.error("getLastRentalsForBicycle", e);
            }
        } else {
            log.warn("bikeshare-rentals cannot be discovered");
        }

        return Optional.empty();
    }

    public Optional<List<RentalDTO>> getLastRentalsForBicycleFallback(Integer bicycleId, Integer limit) {
        return Optional.empty();
    }
}
