package org.inigma.shared.mongo;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Mongo DAO Templating framework. Similar in intent to the spring jdbc template.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public abstract class MongoDaoTemplate<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MongoOperations mongo;
    protected String collection;
    protected Class<T> template;

    public MongoDaoTemplate(MongoOperations operations, String collection, Class<T> template) {
        this.mongo = operations;
        this.collection = collection;
        this.template = template;
    }
    
    public MongoDaoTemplate(String collection, Class<T> template) {
        this.collection = collection;
        this.template = template;
    }

    /**
     * A query retrieving all documents in the collection.
     */
    public Collection<T> find() {
        return mongo.findAll(template, collection);
    }

    /**
     * A query retrieving all documents in the collection up to the specified limit.
     */
    public Collection<T> find(int limit) {
        throw new UnsupportedOperationException("Limit currently not supported");
//        return mongo.findAll(template, collection);
    }

    /**
     * A very simplistic query with only a single key/value pairing.
     */
    public Collection<T> find(String key, Object value) {
        return mongo.find(Query.query(Criteria.where(key).is(value)), template, key);
    }

    /**
     * A very simple retrieval of data given the internal object id.
     */
    public T findById(Serializable id) {
        return mongo.findOne(Query.query(Criteria.where("_id").is(id)), template, collection);
    }

    protected String generateId() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    protected void upsert(T object) {
        mongo.save(object, collection);
    }
}
