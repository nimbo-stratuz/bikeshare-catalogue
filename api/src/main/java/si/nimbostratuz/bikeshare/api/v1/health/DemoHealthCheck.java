package si.nimbostratuz.bikeshare.api.v1.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import si.nimbostratuz.bikeshare.services.configuration.BikeshareConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class DemoHealthCheck implements HealthCheck {

    @Inject
    private BikeshareConfig bikeshareConfig;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.named(DemoHealthCheck.class.getSimpleName());

        if (bikeshareConfig.getHealthy()) {
            return builder.up().build();
        } else {
            return builder.down().build();
        }
    }
}
