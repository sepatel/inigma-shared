package org.inigma.shared.webapp;

public class ExceptionResponse {
    private Exception exception;
    
    public ExceptionResponse(Exception e) {
        this.exception = e;
    }

    public String getMessage() {
        return exception.getMessage();
    }

    public String getError() {
        return exception.getClass().getName();
    }
}
