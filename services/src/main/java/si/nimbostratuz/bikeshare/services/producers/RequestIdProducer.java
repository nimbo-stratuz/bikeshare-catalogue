package si.nimbostratuz.bikeshare.services.producers;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import si.nimbostratuz.bikeshare.models.common.RequestId;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class RequestIdProducer {

    private Logger log = LogManager.getLogger(RequestIdProducer.class.getName());

    @Inject
    private HttpServletRequest request;

    @Produces
    @RequestScoped
    public RequestId getRequestId() {

        RequestId requestId = new RequestId();

        String currentRequestId = request.getHeader("X-Request-ID");

        if (currentRequestId != null) {
            requestId.set(currentRequestId);
        } else {
            log.debug("No X-Request-ID on request. Generating new UUID.");
            requestId.set(UUID.randomUUID().toString());
        }

        return requestId;
    }

}
