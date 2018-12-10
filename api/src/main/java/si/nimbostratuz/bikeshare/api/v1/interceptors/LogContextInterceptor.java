package si.nimbostratuz.bikeshare.api.v1.interceptors;

import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.logging.log4j.CloseableThreadContext;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.UUID;

@Log
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class LogContextInterceptor {

    @AroundInvoke
    public Object logMethodEntryAndExit(InvocationContext context) throws Exception {

        var configurationUtil = ConfigurationUtil.getInstance();

        var settings = new HashMap<String, String>();

        settings.put("environmentType", configurationUtil.get("kumuluzee.env.name").orElse(null));
        settings.put("applicationName", configurationUtil.get("kumuluzee.name").orElse(null));
        settings.put("applicationVersion", configurationUtil.get("kumuluzee.version").orElse(null));
        settings.put("clusterId", configurationUtil.get("kumuluzee.discovery.cluster").orElse(null));
        settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());

        // TODO: Check HTTP headers for existing request UUID
        settings.put("uniqueRequestId", UUID.randomUUID().toString());

        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(settings)) {
            return context.proceed();
        }
    }
}
