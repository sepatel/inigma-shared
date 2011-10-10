package org.inigma.shared.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

/**
 * Mongo DAO Templating framework. Similar in intent to the spring jdbc template.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public abstract class MongoDaoTemplate<T> {
    protected Log logger = LogFactory.getLog(getClass());

    protected final MongoDataStore pool;
    protected final String collection;

    protected MongoDaoTemplate() {
        // for @Cacheable annotation support. Should never be used actually.
        this(null, null);
    }

    public MongoDaoTemplate(MongoDataStore pool, String collection) {
        this.pool = pool;
        this.collection = collection;
    }

    protected Collection<T> convert(final DBCursor cursor) {
        return new ArrayList<T>() {
            @Override
            public boolean contains(Object o) {
                throw new UnsupportedOperationException("This collection is a cursor");
            }

            @Override
            protected void finalize() throws Throwable {
                cursor.close();
                super.finalize();
            }

            @Override
            public boolean isEmpty() {
                return cursor.size() == 0;
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
                        return convert(iterator.next());
                    }

                    @Override
                    public void remove() {
                        iterator.remove();
                    }
                };
            }

            @Override
            public int size() {
                return cursor.size();
            }
        };
    }

    protected final T convert(DBObject data) {
        if (data == null) {
            return null;
        }
        return convert(new DBObjectWrapper(data));
    }

    protected abstract T convert(DBObjectWrapper data);

    /**
     * A query retrieving all documents in the collection.
     */
    public Collection<T> find() {
        return convert(getCollection(true).find());
    }

    /**
     * A query retrieving all documents in the collection up to the specified limit.
     */
    public Collection<T> find(int limit) {
        return convert(getCollection(true).find().limit(limit));
    }

    public Collection<T> find(Map<String, Object> params) {
        BasicDBObject query = new BasicDBObject(params);
        return convert(getCollection(true).find(query));
    }

    /**
     * A very simplistic query with only a single key/value pairing.
     */
    public Collection<T> find(String key, Object value) {
        BasicDBObject query = new BasicDBObject(key, value);
        return convert(getCollection(true).find(query));
    }

    /**
     * A very simple retrieval of data given the internal object id.
     */
    public T findById(Serializable id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        return convert(getCollection(true).findOne(query));
    }

    public Collection<T> findByIds(Collection<?> ids) {
        BasicDBObject inClause = new BasicDBObject("$in", ids);
        BasicDBObject query = new BasicDBObject("_id", inClause);
        return convert(getCollection(true).find(query));
    }

    protected String generateId() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    public DBCollection getCollection() {
        return pool.getCollection(collection);
    }

    public DBCollection getCollection(boolean slave) {
        return pool.getCollection(collection, slave);
    }

    protected void throwOnError(WriteResult result) {
        CommandResult lastError = result.getCachedLastError();
        if (lastError != null) {
            try {
                lastError.throwOnError();
            } catch (MongoException e) {
                logger.error(lastError.getErrorMessage(), lastError.getException());
                throw e;
            }
        }
    }
}
