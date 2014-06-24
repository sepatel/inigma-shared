package org.inigma.shared.mongo;

import com.mongodb.AggregationOutput;
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
public class AggregationOutputConvertingCollection<T> extends AbstractCollection<T> {
    private final AggregationOutput cursor;
    private final Converter<DBObject, T> converter;
    private int size = -1;

    public AggregationOutputConvertingCollection(final AggregationOutput ref, final Converter<DBObject, T> converter) {
        this.cursor = ref;
        this.converter = converter;
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<DBObject> iterator = cursor.results().iterator();
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
    public synchronized int size() {
        if (size == -1) {
            size = 0;
            for (DBObject o : cursor.results()) {
                size++;
            }
        }
        return size;
    }
}
