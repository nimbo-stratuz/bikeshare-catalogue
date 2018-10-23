package si.nimbostratuz.bikeshare.models.common;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Location {

    @Column(name = "location_lat", nullable = false)
    private Double latitude;

    @Column(name = "location_lng", nullable = false)
    private Double longitude;
}
