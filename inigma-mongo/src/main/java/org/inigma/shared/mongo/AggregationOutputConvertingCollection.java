package org.inigma.shared.mongo;

import com.mongodb.AggregationOutput;
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
public class AggregationOutputConvertingCollection<T> extends IterableConverter<DBObject, T> {
    private int size = -1;

    public AggregationOutputConvertingCollection(final AggregationOutput ref, final Converter<DBObject, T> converter) {
        super(ref.results(), converter);
    }

    @Override
    public synchronized int size() {
        if (size == -1) {
            size = 0;
            for (DBObject o : getSource()) {
                size++;
            }
        }
        return size;
    }
}
