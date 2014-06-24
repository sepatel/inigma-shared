package org.inigma.shared.mongo;

import com.mongodb.DBObject;

import org.springframework.core.convert.converter.Converter;

/**
 * This is in case a simple wrapping of the DBCursor into a Collection is desired then the NoOpConverter may be used.
 *
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 6/24/14 11:25 AM
 */
public class NoOpConverter implements Converter<DBObject, DBObject> {
    public static final NoOpConverter SINGLETON = new NoOpConverter();

    @Override
    public DBObject convert(DBObject source) {
        return source;
    }
}
