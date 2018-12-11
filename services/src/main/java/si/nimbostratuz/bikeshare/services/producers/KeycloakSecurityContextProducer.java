package si.nimbostratuz.bikeshare.services.producers;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class KeycloakSecurityContextProducer {

    @Inject
    private HttpServletRequest request;

    @Produces
    @RequestScoped
    public KeycloakSecurityContext getAccessToken() {

        return ((KeycloakPrincipal) request.getUserPrincipal()).getKeycloakSecurityContext();
    }
}
