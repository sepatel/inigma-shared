package org.inigma.shared.webapp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 1/18/14 6:21 AM
 */
public class ServletRequestBodyWrapper extends HttpServletRequestWrapper {
    private final byte[] requestBody;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public ServletRequestBodyWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.requestBody = IOUtils.toString(request.getInputStream()).getBytes();
    }

    public String getBody() {
        return new String(requestBody);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(requestBody)));
    }
}
