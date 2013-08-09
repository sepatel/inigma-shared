package org.inigma.shared.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.InterruptibleChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 7/10/13 11:26 AM
 */
public class LoggingRestTemplate extends RestTemplate implements ClientHttpRequestInterceptor {
    private static final WebServiceLogHandler DEFAULT_HANDLER = new WebServiceLogHandler() {
        @Override
        public void onWebServiceLog(WebServiceLog log) {
            // noop();
        }
    };
    private WebServiceLogHandler handler;

    public LoggingRestTemplate() {
        this.handler = DEFAULT_HANDLER;
        setInterceptors(Collections.EMPTY_LIST);
    }

    public LoggingRestTemplate(ClientHttpRequestFactory requestFactory) {
        this();
        setRequestFactory(requestFactory);
    }

    public LoggingRestTemplate(WebServiceLogHandler handler) {
        this();
        this.handler = handler;
    }

    public LoggingRestTemplate(WebServiceLogHandler handler, ClientHttpRequestFactory factory) {
        this(factory);
        this.handler = handler;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        WebServiceLog log = new WebServiceLog();
        log.setUrl(req.getURI().toURL().toString());
        log.setMethod(req.getMethod());
        if (body.length > 0) {
            log.setRequest(new String(body));
        }
        log.setTimestamp(new Date());
        try {
            ClientHttpResponse httpResponse = new HttpResponseWrapper(execution.execute(req, body));
            log.setStatus(httpResponse.getRawStatusCode());
            StringBuilder sb = new StringBuilder();
            InputStream httpResponseBody = httpResponse.getBody();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = httpResponseBody.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, read));
            }
            log.setResponse(sb.toString());
            return httpResponse;
        } catch (IOException e) {
            log.setStatus(-1);
            log.setResponse(e.getMessage());
            throw e;
        } finally {
            log.setDuration((int) (System.currentTimeMillis() - log.getTimestamp().getTime()));
            handler.onWebServiceLog(log);
        }
    }

    public void setHandler(WebServiceLogHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        ArrayList<ClientHttpRequestInterceptor> newList = new ArrayList<ClientHttpRequestInterceptor>(interceptors.size() + 1);
        newList.add(this);
        newList.addAll(interceptors);
        super.setInterceptors(newList);
    }
}
