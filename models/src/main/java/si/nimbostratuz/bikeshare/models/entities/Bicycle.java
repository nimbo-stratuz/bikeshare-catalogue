package si.nimbostratuz.bikeshare.models.entities;

import lombok.Data;
import si.nimbostratuz.bikeshare.models.common.Location;
import si.nimbostratuz.bikeshare.models.common.Owner;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "bicycle")
@Data
public class Bicycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(targetEntity = SmartLock.class, fetch = FetchType.EAGER)
    private SmartLock smartLock;

    @Embedded
    private Location location;

    @Column(name = "available", nullable = false)
    private Boolean available;

    private Instant dateAdded;

    @Embedded
    private Owner ownerId;
}
