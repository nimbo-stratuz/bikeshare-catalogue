package si.nimbostratuz.bikeshare.services.producers;


import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class AccessTokenProducer {

    @Inject
    private HttpServletRequest request;

    @Produces
    @RequestScoped
    public AccessToken getAccessToken() {
        KeycloakSecurityContext securityContext =
                ((KeycloakPrincipal) request.getUserPrincipal()).getKeycloakSecurityContext();

        return securityContext.getToken();
    }
}
