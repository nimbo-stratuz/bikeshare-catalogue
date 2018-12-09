package si.nimbostratuz.bikeshare.models.common;

import lombok.Data;
import si.nimbostratuz.bikeshare.models.entities.Bicycle;

@Data
public class AuthenticatedUser {

    private String id;

    private String username;

    public boolean owns(Bicycle bicycle) {
        return this.id.equals(bicycle.getOwnerId());
    }
}
