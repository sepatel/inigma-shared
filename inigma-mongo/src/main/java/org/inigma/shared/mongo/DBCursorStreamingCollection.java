package org.inigma.shared.mongo;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import org.inigma.shared.wrapper.IterableConverter;
import org.inigma.shared.wrapper.NoOpConverter;

/**
 * Purpose is to a wrapper for the native DBObject of into a collection for streaming access.
 * This is especially useful when wanting to stream the response of a db cursor or start working on it prior to having
 * collected all of the data out of it as is the case with spring data mongo.
 *
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 6/24/14 11:14 AM
 */
public class DBCursorStreamingCollection extends IterableConverter<DBObject, DBObject> {
    private final DBCursor cursor;

    public DBCursorStreamingCollection(DBCursor ref) {
        super(ref, NoOpConverter.SINGLETON);
        this.cursor = ref;
    }

    @Override
    public int size() {
        return cursor.count();
    }
}
