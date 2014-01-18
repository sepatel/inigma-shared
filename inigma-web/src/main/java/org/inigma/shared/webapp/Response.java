package org.inigma.shared.webapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 1/4/14 11:42 AM
 */
public class Response extends ResponseEntity<Object> {
    public Response(Object body) {
        this(body, HttpStatus.OK);
    }

    public Response(Object body, HttpStatus status) {
        super(body, status);
    }
}
