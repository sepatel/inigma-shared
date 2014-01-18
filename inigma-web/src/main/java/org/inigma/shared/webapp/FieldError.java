package org.inigma.shared.webapp;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

class FieldError extends GlobalError {
    private final String field;

    public FieldError(String field, String code, String message) {
        super(code, message);
        Preconditions.checkNotNull(field);
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldError)) return false;
        if (!super.equals(o)) return false;

        FieldError error = (FieldError) o;

        if (!field.equals(error.field)) return false;

        return true;
    }

    public final String getField() {
        return field;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + field.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("field", field)
                .toString();
    }
}
