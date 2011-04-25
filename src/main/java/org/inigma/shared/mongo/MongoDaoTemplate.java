package org.inigma.shared.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * Mongo DAO Templating framework. Similar in intent to the spring jdbc template.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public abstract class MongoDaoTemplate<T> {
    private static Log logger = LogFactory.getLog(MongoDaoTemplate.class);

    protected final MongoDataStore pool;
    protected final String collection;

    @Autowired
    public MongoDaoTemplate(MongoDataStore pool, String collection) {
        this.pool = pool;
        this.collection = collection;
    }

    /**
     * A query retrieving all documents in the collection.
     */
    public Collection<T> find() {
        return convert(getCollection(false).find());
    }

    /**
     * A query retrieving all documents in the collection up to the specified limit.
     */
    public Collection<T> find(int limit) {
        return convert(getCollection(false).find().limit(limit));
    }

    public Collection<T> find(Map<String, Object> params) {
        BasicDBObject query = new BasicDBObject(params);
        return convert(getCollection(false).find(query));
    }

    /**
     * A very simplistic query with only a single key/value pairing.
     */
    public Collection<T> find(String key, Object value) {
        BasicDBObject query = new BasicDBObject(key, value);
        return convert(getCollection(false).find(query));
    }

    /**
     * A very simple retrieval of data given the internal object id.
     */
    public T findById(Serializable id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        return convert(getCollection(false).findOne(query));
    }

    public Collection<T> findByIds(Collection<?> ids) {
        BasicDBObject inClause = new BasicDBObject("$in", ids);
        BasicDBObject query = new BasicDBObject("_id", inClause);
        return convert(getCollection(false).find(query));
    }

    public DBCollection getCollection() {
        return pool.getCollection(collection);
    }

    public DBCollection getCollection(boolean slave) {
        return pool.getCollection(collection, slave);
    }

    protected Collection<T> convert(final DBCursor cursor) {
        return new ArrayList<T>() {
            @Override
            public boolean isEmpty() {
                return cursor.size() == 0;
            }

            @Override
            public boolean contains(Object o) {
                throw new UnsupportedOperationException("This collection is a cursor");
            }

            @Override
            public int size() {
                return cursor.size();
            }

            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        boolean next = cursor.hasNext();
                        if (!next) {
                            cursor.close();
                        }
                        return next;
                    }

                    @Override
                    public T next() {
                        return convert(cursor.next());
                    }

                    @Override
                    public void remove() {
                        cursor.remove();
                    }
                };
            }

            @Override
            protected void finalize() throws Throwable {
                cursor.close();
                super.finalize();
            }
        };
    }

    protected final T convert(DBObject data) {
        return convert(new DBObjectWrapper(data));
    }

    protected abstract T convert(DBObjectWrapper data);

    protected void throwOnError(WriteResult result) {
        CommandResult lastError = result.getLastError();
        if (lastError != null) {
            logger.error(lastError.getErrorMessage(), lastError.getException());
            lastError.throwOnError();
        }
    }
}
