package org.inigma.shared.message;

import java.util.Date;

public class Message {
    private String code;
    private String value;
    private Date modified;

    public String getCode() {
        return code;
    }

    public Date getModifiedDate() {
        if (modified == null) {
            modified = new Date();
        }
        return modified;
    }

    public String getValue() {
        return value;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
