package si.nimbostratuz.bikeshare.models.common;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;

public class RequestId {

    private static final Logger log = LogManager.getLogger(RequestId.class.getName());

    private String requestId;

    public String get() {
        return requestId;
    }

    public void set(String requestId) {
        if (this.requestId != null) {
            this.requestId = requestId;
        } else {
            log.warn("Attempted to set RequestId twice");
        }
    }
}
