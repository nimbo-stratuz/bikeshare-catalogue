package si.nimbostratuz.bikeshare.api.v1.interceptors;

import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.logging.log4j.CloseableThreadContext;
import si.nimbostratuz.bikeshare.models.common.RequestId;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;

@Log
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class LogContextInterceptor {

    @Inject
    private RequestId requestId;

    @AroundInvoke
    public Object logMethodEntryAndExit(InvocationContext context) throws Exception {

        var configurationUtil = ConfigurationUtil.getInstance();

        var settings = new HashMap<String, String>();

        settings.put("environmentType", configurationUtil.get("kumuluzee.env.name").orElse(null));
        settings.put("applicationName", configurationUtil.get("kumuluzee.name").orElse(null));
        settings.put("applicationVersion", configurationUtil.get("kumuluzee.version").orElse(null));
        settings.put("clusterId", configurationUtil.get("kumuluzee.discovery.cluster").orElse(null));
        settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());

        settings.put("uniqueRequestId", requestId.get());

        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(settings)) {
            return context.proceed();
        }
    }
}
