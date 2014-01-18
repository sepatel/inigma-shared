package org.inigma.shared.webapp;

@Deprecated
public class ExceptionResponse {
    private Exception exception;

    public ExceptionResponse(Exception e) {
        this.exception = e;
    }

    public String getError() {
        return exception.getClass().getName();
    }

    public String getMessage() {
        return exception.getMessage();
    }
}
