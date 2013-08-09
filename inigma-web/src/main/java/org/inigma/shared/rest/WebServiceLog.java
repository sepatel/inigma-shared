package org.inigma.shared.rest;

import java.util.Date;

import org.springframework.http.HttpMethod;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 8/8/13 1:10 PM
 */
public class WebServiceLog {
    private Date timestamp;
    private int duration; // in milliseconds
    private HttpMethod method; // get, post, etc ...
    private String url;
    private String request; // for post/put what to send
    private int status;
    private String response;

    public int getDuration() {
        return duration;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
