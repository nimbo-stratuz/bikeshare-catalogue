package si.nimbostratuz.bikeshare.services.producers;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import si.nimbostratuz.bikeshare.models.common.AuthenticatedUser;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@RequestScoped
public class AuthenticatedUserProducer {

    @Inject
    private AccessToken accessToken;

    @Inject
    private KeycloakSecurityContext securityContext;

    @Produces
    @RequestScoped
    public AuthenticatedUser getAuthenticatedUser() {

        AuthenticatedUser user = new AuthenticatedUser();

        user.setId(accessToken.getSubject());
        user.setUsername(accessToken.getPreferredUsername());
        user.setAuthorizationToken(securityContext.getTokenString());

        return user;
    }
}