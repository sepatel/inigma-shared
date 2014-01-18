package org.inigma.shared.webapp;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

class GlobalError {
    private final String code;
    private String message;

    public GlobalError(String code, String message) {
        Preconditions.checkNotNull(code);
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlobalError)) return false;

        GlobalError that = (GlobalError) o;

        if (!code.equals(that.code)) return false;

        return true;
    }

    public final String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("code", code)
                .add("message", message)
                .toString();
    }

    void setMessage(String message) {
        this.message = message;
    }
}
