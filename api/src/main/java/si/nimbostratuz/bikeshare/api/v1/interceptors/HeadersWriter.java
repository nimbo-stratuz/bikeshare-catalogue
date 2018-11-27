package si.nimbostratuz.bikeshare.api.v1.interceptors;

import com.kumuluz.ee.common.runtime.EeRuntime;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

@Provider
public class HeadersWriter implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
        writerInterceptorContext.getHeaders()
                                .add("X-KumuluzEE-Instance-ID", EeRuntime.getInstance().getInstanceId());

        writerInterceptorContext.proceed();
    }
}
