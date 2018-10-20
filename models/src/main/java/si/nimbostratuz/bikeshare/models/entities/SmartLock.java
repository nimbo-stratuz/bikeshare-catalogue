package si.nimbostratuz.bikeshare.models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "smart_lock")
@Data
public class SmartLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(targetEntity = Bicycle.class, fetch = FetchType.EAGER)
    private Bicycle bicycle;

    @Column(name = "owner_id")
    private Integer ownerId;
}
