package org.inigma.shared.mongo;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.springframework.core.convert.converter.Converter;

/**
 * Collection to provide real time conversion of data from the native DBObject of mongo into the target object desired.
 * This is especially useful when wanting to stream the response of a db cursor or start working on it prior to having
 * collected all of the data out of it as is the case with spring data mongo.
 *
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 6/24/14 11:14 AM
 */
public class DBCursorConvertingCollection<T> extends AbstractCollection<T> {
    private final DBCursor cursor;
    private final Converter<DBObject, T> converter;

    public DBCursorConvertingCollection(final DBCursor ref, final Converter<DBObject, T> converter) {
        this.cursor = ref;
        this.converter = converter;
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<DBObject> iterator = cursor.iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return converter.convert(iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    @Override
    public int size() {
        return cursor.count();
    }
}
