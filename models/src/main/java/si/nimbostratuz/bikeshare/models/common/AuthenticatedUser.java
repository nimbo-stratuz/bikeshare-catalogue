package si.nimbostratuz.bikeshare.models.common;

import lombok.Data;

@Data
public class AuthenticatedUser {

    private String id;

    private String username;
}
