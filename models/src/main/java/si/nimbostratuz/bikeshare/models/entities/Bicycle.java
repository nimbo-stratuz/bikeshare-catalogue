package si.nimbostratuz.bikeshare.models.entities;

import lombok.Data;
import si.nimbostratuz.bikeshare.models.common.Location;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "bicycle")
@Data
public class Bicycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(targetEntity = SmartLock.class, fetch = FetchType.EAGER, optional = false)
    private SmartLock smartLock;

    @Embedded
    private Location location;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "date_added", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;
}
