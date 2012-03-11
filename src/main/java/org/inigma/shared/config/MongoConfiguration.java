package org.inigma.shared.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.inigma.shared.mongo.MongoDataStore;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
    private static final String VALUE = "value";
    private static final Timer TIMER = new Timer(true);

    private final MongoDataStore ds;
    private final String collection;
    private TimerTask reloadTask;

    @Autowired
    public MongoConfiguration(MongoDataStore ds) {
        this(ds, "config");
    }

    public MongoConfiguration(MongoDataStore ds, String collection) {
        this.ds = ds;
        this.collection = collection;

        reload();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#remove(java.lang.String)
     */
    @Override
    public Object remove(String key) {
        ds.getCollection(collection).remove(new BasicDBObject("_id", key));
        return super.remove(key);
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
    protected Object getValue(String key) {
        DBObject query = new BasicDBObject(KEY, key);
        DBObject dbObject = ds.getCollection(collection, true).findOne(query);
        if (dbObject == null) {
            throw new IllegalStateException("Configuration " + key + " not found!");
        }
        return dbObject.get(VALUE);
    }

    @Override
    protected void setValue(String key, Object value) {
        DBObject query = new BasicDBObject(KEY, key);
        DBObject data = new BasicDBObject(KEY, key);
        data.put(VALUE, value);
        ds.getCollection(collection).update(query, data, true, false);
    }

    private void reload() {
        Map<String, Object> newconfigs = new HashMap<String, Object>();
        DBCursor allConfigs = ds.getCollection(collection, true).find();
        for (DBObject o : allConfigs) {
            newconfigs.put((String) o.get(KEY), o.get(VALUE));
        }
        super.reload(newconfigs);
    }
}
