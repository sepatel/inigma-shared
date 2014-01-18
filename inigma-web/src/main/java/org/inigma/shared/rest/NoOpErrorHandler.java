package org.inigma.shared.rest;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 12/21/13 1:01 AM
 */
public class NoOpErrorHandler implements ResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // Do not handle the errors differently
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getRawStatusCode() >= 400 || response.getRawStatusCode() <= 0;
    }
}
