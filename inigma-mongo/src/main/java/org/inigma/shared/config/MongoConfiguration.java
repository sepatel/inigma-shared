package org.inigma.shared.config;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.ClassUtils;

/**
 * Dynamically loads and reloads configuration settings from a collection. The data model is presumed to be in the
 * following syntax.
 * <p/>
 * <pre>
 * {_id: string, value: object}
 * </pre>
 *
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class MongoConfiguration extends AbstractConfiguration {
    private static final Timer TIMER = new Timer(true);
    private static final Logger logger = LoggerFactory.getLogger(MongoConfiguration.class);
    private final MongoOperations mongo;
    private final String collection;
    private TimerTask reloadTask;
    private String application;

    @Autowired
    public MongoConfiguration(MongoOperations ds) {
        this(ds, "config");
    }

    public MongoConfiguration(MongoOperations ds, String collection) {
        this.mongo = ds;
        this.collection = collection;

        reload();
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
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
        DBObject object = mongo.getCollection(collection).findOne(findByIdQuery(key));
        if (object == null) {
            return null;
        }
        return convertValueToResult(object.get("value"), type);
    }

    @Override
    protected void removeValue(String key) {
        mongo.getCollection(collection).remove(findByIdQuery(key), WriteConcern.JOURNAL_SAFE);
    }

    @Override
    protected void setValue(String key, Object value) {
        BasicDBObject query = findByIdQuery(key);
        BasicDBObject data = findByIdQuery(key); // want a new copy not a shared copy
        if (value == null || ClassUtils.isPrimitiveOrWrapper(value.getClass())
                || ClassUtils.isAssignableValue(String.class, value) || ClassUtils.isAssignableValue(Date.class, value)) {
            data.append("value", value);
        } else if (ClassUtils.isAssignableValue(List.class, value)) {
            BasicDBList sink = new BasicDBList();
            mongo.getConverter().write(value, sink);
            data.append("value", sink);
        } else { // complex object type
            BasicDBObject sink = new BasicDBObject();
            mongo.getConverter().write(value, sink);
            data.append("value", sink);
        }

        mongo.getCollection(collection).update(query, new BasicDBObject("$set", data), true, false, WriteConcern.JOURNAL_SAFE);
    }

    private <T> T convertValueToResult(Object value, Class<T> type) {
        if (value == null) {
            return null;
        } else if ((type != null && ClassUtils.isAssignableValue(type, value))
                || ClassUtils.isPrimitiveOrWrapper(value.getClass())
                || ClassUtils.isAssignableValue(String.class, value) || ClassUtils.isAssignableValue(Date.class, value)) {
            return (T) value;
        } else if (value instanceof DBObject && ((DBObject) value).containsField("_class")) { // strongly typed data
            DBObject classData = (DBObject) value;
            try {
                type = (Class<T>) Class.forName((String) classData.get("_class"));
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class " + classData.get("_class") + " not found in classpath!");
            }
        } else if (type == null) { // this is intentionally after the _class check
            return (T) value;
        }

        return mongo.getConverter().read(type, (DBObject) value);
    }

    private BasicDBObject findByIdQuery(String key) {
        BasicDBObject query = new BasicDBObject("_id", key);
        if (application != null) {
            query.append("$or", Arrays.asList(
                    new BasicDBObject("app", null),
                    new BasicDBObject("app", application)
            ));
        }
        return query;
    }

    private void reload() {
        BasicDBObject query = new BasicDBObject("$or", Arrays.asList(
                new BasicDBObject("app", null),
                new BasicDBObject("app", application)
        ));
        Map<String, Object> newconfigs = new HashMap<>();
        for (DBObject entry : mongo.getCollection(collection).find(query)) {
            try {
                newconfigs.put((String) entry.get("_id"), convertValueToResult(entry.get("value"), null));
            } catch (RuntimeException e) {
                logger.error("Unable to update configuration {}", entry.get("_id"), e);
            }
        }
        super.reload(newconfigs);
    }
}
