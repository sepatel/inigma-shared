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
    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected MongoOperations mongo;

    protected Class<T> template;

    public MongoDaoTemplate(MongoOperations operations, Class<T> template) {
        this.mongo = operations;
        this.template = template;
    }

    public MongoDaoTemplate( Class<T> template) {
        this.template = template;
    }

    /**
     * A very simplistic query with only a single key/value pairing.
     */
    public Collection<T> find(String where, Object is) {
        return mongo.find(Query.query(Criteria.where(where).is(is)), template);
    }

    /**
     * A very simple retrieval of data given the internal object id.
     */
    public T findById(Serializable id) {
        return mongo.findOne(Query.query(Criteria.where("_id").is(id)), template);
    }

    public T remove(Serializable id) {
        return mongo.findAndRemove(Query.query(Criteria.where("_id").is(id)), template);
    }

    protected void upsert(T object) {
        mongo.save(object);
    }
}
