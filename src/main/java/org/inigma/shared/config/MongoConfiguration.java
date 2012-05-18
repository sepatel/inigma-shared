package org.inigma.shared.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

/**
 * Dynamically loads and reloads configuration settings from a collection. The data model is presumed to be in the
 * following syntax.
 * 
 * <pre>
 * {_id: string, value: object}
 * </pre>
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class MongoConfiguration extends AbstractConfiguration {
    private static final String KEY = "_id";
    private static final Timer TIMER = new Timer(true);

    private final MongoOperations mongo;
    private final String collection;
    private TimerTask reloadTask;

    @Autowired
    public MongoConfiguration(MongoOperations ds) {
        this(ds, "config");
    }

    public MongoConfiguration(MongoOperations ds, String collection) {
        this.mongo = ds;
        this.collection = collection;

        reload();
    }

    public void setPollingFrequency(long pollingFrequency) {
        if (reloadTask != null) {
            reloadTask.cancel();
        }
        if (pollingFrequency > 0) {
            reloadTask = new TimerTask() {
                @Override
                public void run() {
                    reload();
                }
            };
            TIMER.schedule(reloadTask, pollingFrequency, pollingFrequency);
        }
    }

    @Override
    protected <T> T getValue(String key, Class<T> type) {
        DBObject object = mongo.getCollection(collection).findOne(new BasicDBObject(KEY, key));
        /*
        ConfigurationEntry entry = mongo.findOne(Query.query(Criteria.where(KEY).is(key)), ConfigurationEntry.class, collection);
        if (entry == null) {
            throw new IllegalStateException("Configuration " + key + " not found!");
        }
        */
        if (object == null) {
            return null;
        }
        return (T) object.get("value");
    }

    @Override
    protected void removeValue(String key) {
        mongo.remove(Query.query(Criteria.where(KEY).is(key)), collection); // TODO: Journal safe this bit
    }

    @Override
    protected void setValue(String key, Object value) {
        BasicDBObject data = new BasicDBObject(KEY, key);
        data.append("value", value);
        mongo.getCollection(collection).update(new BasicDBObject(KEY, key), data, true, false, WriteConcern.JOURNAL_SAFE);
    }

    private void reload() {
        Map<String, Object> newconfigs = new HashMap<String, Object>();
        for (DBObject entry : mongo.getCollection(collection).find()) {
            newconfigs.put((String) entry.get(KEY), entry.get("value"));
        }
        super.reload(newconfigs);
    }
}
