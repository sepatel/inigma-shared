package org.inigma.shared.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 8/8/13 1:32 PM
 */
class HttpResponseWrapper implements ClientHttpResponse {
    private final ClientHttpResponse response;
    private byte[] body;

    HttpResponseWrapper(ClientHttpResponse response) {
        this.response = response;
    }

    public void close() {
        this.response.close();
    }

    public InputStream getBody() throws IOException {
        if (this.body == null) {
            this.body = FileCopyUtils.copyToByteArray(this.response.getBody());
        }
        return new ByteArrayInputStream(this.body);
    }

    public HttpHeaders getHeaders() {
        return this.response.getHeaders();
    }

    public int getRawStatusCode() throws IOException {
        return this.response.getRawStatusCode();
    }

    public HttpStatus getStatusCode() throws IOException {
        return this.response.getStatusCode();
    }

    public String getStatusText() throws IOException {
        return this.response.getStatusText();
    }
}

