package si.nimbostratuz.bikeshare.services.configuration;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import lombok.Data;

import javax.enterprise.context.ApplicationScoped;

@Data
@ApplicationScoped
@ConfigBundle("bikeshare-config")
public class BikeshareConfig {

    @ConfigValue(value = "n-rentals-included", watch = true)
    private Integer nRentalsIncluded;

    private Boolean healthy;

    @ConfigValue(value = "rentals-service-timeout", watch = true)
    private Integer rentalsServiceTimeout;
}
