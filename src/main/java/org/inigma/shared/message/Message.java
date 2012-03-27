package org.inigma.shared.message;

import java.io.Serializable;

public class Message {
    static class MessageId implements Serializable {
        private String code;
        private String locale;

        public String getCode() {
            return code;
        }

        public String getLocale() {
            return locale;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }
    }

    private MessageId id;
    private String value;

    public Message() {
        this.id = new MessageId();
    }

    public Message(MessageResponse request) {
        this.id = new MessageId();
        this.id.code = request.getCode();
        this.id.locale = request.getLocale();
        this.value = request.getValue();
    }

    public MessageId getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setId(MessageId id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
