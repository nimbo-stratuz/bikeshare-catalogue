package si.nimbostratuz.bikeshare.services.producers;


import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class AccessTokenProducer {

    @Inject
    private KeycloakSecurityContext securityContext;

    @Produces
    @RequestScoped
    public AccessToken getAccessToken() {

        return securityContext.getToken();
    }
}
