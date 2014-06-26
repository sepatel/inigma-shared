package org.inigma.shared.mongo;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import org.inigma.shared.wrapper.IterableConverter;
import org.springframework.core.convert.converter.Converter;

/**
 * Collection to provide real time conversion of data from the native DBObject of mongo into the target object desired.
 * This is especially useful when wanting to stream the response of a db cursor or start working on it prior to having
 * collected all of the data out of it as is the case with spring data mongo.
 *
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 6/24/14 11:14 AM
 */
public class DBCursorConvertingCollection<T> extends IterableConverter<DBObject, T> {
    private final DBCursor cursor;

    public DBCursorConvertingCollection(DBCursor ref, Converter<DBObject, T> converter) {
        super(ref, converter);
        this.cursor = ref;
    }

    @Override
    public int size() {
        return cursor.count();
    }
}
