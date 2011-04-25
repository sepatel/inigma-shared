package org.inigma.shared.config;

import java.util.*;

import org.inigma.shared.mongo.MongoDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import com.mongodb.*;

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
public class Configuration {
    private static final String KEY = "_id";
    private static final String VALUE = "value";
    private static final Timer TIMER = new Timer(true);

    private final Map<String, Object> configs;
    private final Map<String, Set<ConfigurationObserver>> observers;
    private final MongoDataStore ds;
    private final String collection;

    @Autowired
    public Configuration(MongoDataStore ds, String collection, long pollingFrequency) {
        this.configs = new HashMap<String, Object>();
        this.observers = new HashMap<String, Set<ConfigurationObserver>>();
        this.ds = ds;
        this.collection = collection;

        reload();

        if (pollingFrequency > 0) {
            TIMER.schedule(new TimerTask() {
                @Override
                public void run() {
                    reload();
                }
            }, pollingFrequency, pollingFrequency);
        }
    }

    public Boolean getBoolean(String key, Boolean defaultValue, ConfigurationObserver... listeners) {
        return get(key, defaultValue, listeners);
    }

    public Boolean getBoolean(String key, ConfigurationObserver... listeners) {
        return getBoolean(key, null, listeners);
    }

    public Byte getByte(String key, ConfigurationObserver... listeners) {
        return getByte(key, null, listeners);
    }

    public Byte getByte(String key, Number defaultValue, ConfigurationObserver... listeners) {
        Number value = get(key, defaultValue, listeners);
        if (value == null) {
            return null;
        }
        return value.byteValue();
    }

    public Date getDate(String key, ConfigurationObserver... listeners) {
        return getDate(key, null, listeners);
    }

    public Date getDate(String key, Date defaultValue, ConfigurationObserver... listeners) {
        return get(key, defaultValue, listeners);
    }

    public Double getDouble(String key, ConfigurationObserver... listeners) {
        return getDouble(key, null, listeners);
    }

    public Double getDouble(String key, Number defaultValue, ConfigurationObserver... listeners) {
        Number value = get(key, defaultValue, listeners);
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    public Float getFloat(String key, ConfigurationObserver... listeners) {
        return getFloat(key, null, listeners);
    }

    public Float getFloat(String key, Number defaultValue, ConfigurationObserver... listeners) {
        Number value = get(key, defaultValue, listeners);
        if (value == null) {
            return null;
        }
        return value.floatValue();
    }

    public Integer getInteger(String key, ConfigurationObserver... listeners) {
        return getInteger(key, null, listeners);
    }

    public Integer getInteger(String key, Number defaultValue, ConfigurationObserver... listeners) {
        Number value = get(key, defaultValue, listeners);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    public Set<String> getKeys() {
        return this.configs.keySet();
    }

    public <T> List<T> getList(String key, ConfigurationObserver... listeners) {
        return getList(key, null, listeners);
    }

    public <T> List<T> getList(String key, List<T> defaultValue, ConfigurationObserver... listeners) {
        return get(key, defaultValue, listeners);
    }

    public Long getLong(String key, ConfigurationObserver... listeners) {
        return getLong(key, null, listeners);
    }

    public Long getLong(String key, Number defaultValue, ConfigurationObserver... listeners) {
        Number value = get(key, defaultValue, listeners);
        if (value == null) {
            return null;
        }
        return value.longValue();
    }

    public <T> Map<String, T> getMap(String key, ConfigurationObserver... listeners) {
        return getMap(key, null, listeners);
    }

    public <T> Map<String, T> getMap(String key, Map<String, T> defaultValue, ConfigurationObserver... listeners) {
        return get(key, defaultValue, listeners);
    }

    public Short getShort(String key, ConfigurationObserver... listeners) {
        return getShort(key, null, listeners);
    }

    public Short getShort(String key, Number defaultValue, ConfigurationObserver... listeners) {
        Number value = get(key, defaultValue, listeners);
        if (value == null) {
            return null;
        }
        return value.shortValue();
    }

    public String getString(String key, ConfigurationObserver... listeners) {
        return getString(key, null, listeners);
    }

    public String getString(String key, String defaultValue, ConfigurationObserver... listeners) {
        return get(key, defaultValue, listeners);
    }

    protected <T> T get(String key, T defaultValue, ConfigurationObserver... listeners) {
        if (listeners.length > 0) { // update list of systems listening for changes
            if (!observers.containsKey(key)) {
                observers.put(key, new HashSet<ConfigurationObserver>());
            }
            observers.get(key).addAll(Arrays.asList(listeners));
        }

        if (!configs.containsKey(key)) { // load configuration into the cache if missing
            DBObject query = new BasicDBObject(KEY, key);
            DBObject dbObject = ds.getCollection(collection, true).findOne(query);
            if (dbObject == null) {
                if (defaultValue == null) {
                    throw new IllegalStateException("Configuration " + key + " not found!");
                }
                return defaultValue;
            }
            configs.put(key, dbObject.get(VALUE));
        }

        Object value = configs.get(key);
        if (value == null) {
            return null;
        }
        return (T) value;
    }

    protected boolean set(String key, Object value) {
        Object ovalue = configs.get(key);
        DBObject query = new BasicDBObject(KEY, key);
        DBObject data = new BasicDBObject(KEY, key);
        data.put(VALUE, value);
        WriteResult result = ds.getCollection(collection).update(query, data, true, false);
        configs.put(key, value);
        changed(key, ovalue, value);
        return result.getCachedLastError() != null;
    }

    private void changed(String key, Object ovalue, Object value) {
        if (observers.containsKey(key)) {
            for (ConfigurationObserver observer : observers.get(key)) {
                observer.onConfigurationUpdate(key, ovalue, value);
            }
        }
    }

    private void reload() {
        DBCursor allConfigs = ds.getCollection(collection, true).find();
        for (DBObject o : allConfigs) {
            String k = (String) o.get(KEY);
            Object v = o.get(VALUE);
            Object oldValue = configs.get(k);
            configs.put(k, v);
            if (!valuesAreEqual(oldValue, v)) {
                changed(k, oldValue, v);
            }
        }
    }

    private boolean valuesAreEqual(Object a, Object b) {
        return (a == b || (a == null && b != null) || (a != null && b == null) || a.equals(b));
    }
}
