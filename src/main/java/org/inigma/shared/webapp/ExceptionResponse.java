package org.inigma.shared.webapp;

class ExceptionResponse implements RestResponse {
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
