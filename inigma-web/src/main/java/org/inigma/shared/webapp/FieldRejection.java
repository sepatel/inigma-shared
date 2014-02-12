package org.inigma.shared.webapp;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class FieldRejection extends Rejection {
    private final String field;

    public FieldRejection(String field, String code, String message) {
        super(code, message);
        Preconditions.checkNotNull(field);
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldRejection)) return false;
        if (!super.equals(o)) return false;

        FieldRejection error = (FieldRejection) o;

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
