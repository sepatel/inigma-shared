package org.inigma.shared.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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
        ConfigurationEntry entry = mongo.findOne(Query.query(Criteria.where(KEY).is(key)), ConfigurationEntry.class, collection);
        if (entry == null) {
            throw new IllegalStateException("Configuration " + key + " not found!");
        }
        return (T) entry.getValue();
    }

    @Override
    protected void removeValue(String key) {
        mongo.remove(Query.query(Criteria.where(KEY).is(key)), collection); // TODO: Journal safe this bit
    }

    @Override
    protected void setValue(String key, Object value) {
        ConfigurationEntry entry = new ConfigurationEntry(key, value);
        mongo.save(entry, collection); // TODO: Journal safe this bit
    }

    private void reload() {
        Map<String, Object> newconfigs = new HashMap<String, Object>();
        for (ConfigurationEntry entry : mongo.findAll(ConfigurationEntry.class, collection)) {
            newconfigs.put(entry.getId(), entry.getValue());
        }
        super.reload(newconfigs);
    }
}
