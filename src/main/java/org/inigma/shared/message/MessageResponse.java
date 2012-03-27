package org.inigma.shared.message;

import org.inigma.shared.webapp.RestResponse;

public class MessageResponse implements RestResponse {
    private String code;
    private String locale;
    private String value;

    public MessageResponse() {
    }

    public MessageResponse(Message msg) {
        this.code = msg.getId().getCode();
        this.locale = msg.getId().getLocale();
        this.value = msg.getValue();
    }

    public String getCode() {
        return code;
    }

    public String getLocale() {
        return locale;
    }

    public String getValue() {
        return value;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
