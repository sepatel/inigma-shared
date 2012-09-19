package org.inigma.shared.config;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ClassUtils;

import com.mongodb.BasicDBList;
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
        if (object == null) {
            return null;
        }
        Object value = object.get("value");
        if (type == null || type.isAssignableFrom(value.getClass())) { // matches requested type
            // TODO: Check to see if Date, List, Map, and Object work correctly in this way
            return (T) value;
        }
        if (List.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
            // TODO: Is this piece needed?
        }

        return mongo.getConverter().read(type, (DBObject) value);
    }

    @Override
    protected void removeValue(String key) {
        mongo.remove(Query.query(Criteria.where(KEY).is(key)), collection); // TODO: Journal safe this bit
    }

    @Override
    protected void setValue(String key, Object value) {
        BasicDBObject data = new BasicDBObject(KEY, key);
        if (value == null || ClassUtils.isPrimitiveOrWrapper(value.getClass()) || value.getClass() == String.class
                || Date.class.isAssignableFrom(value.getClass()) || Calendar.class.isAssignableFrom(value.getClass())) {
            data.append("value", value);
        } else if (List.class.isAssignableFrom(value.getClass())) {
            BasicDBList sink = new BasicDBList();
            mongo.getConverter().write(value, sink);
            data.append("value", sink);
        } else { // complex object type
            BasicDBObject sink = new BasicDBObject();
            mongo.getConverter().write(value, sink);
            data.append("value", sink);
        }

        mongo.getCollection(collection).update(new BasicDBObject(KEY, key), data, true, false,
                WriteConcern.JOURNAL_SAFE);
    }

    private void reload() {
        Map<String, Object> newconfigs = new HashMap<String, Object>();
        for (DBObject entry : mongo.getCollection(collection).find()) {
            newconfigs.put((String) entry.get(KEY), entry.get("value"));
        }
        super.reload(newconfigs);
    }
}
