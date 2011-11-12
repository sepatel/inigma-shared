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

    final Map<String, Object> configs;
    private final Set<ConfigurationObserver> observers;
    private final MongoDataStore ds;
    private final String collection;

    @Autowired
    public Configuration(MongoDataStore ds, String collection, long pollingFrequency) {
        this.configs = new HashMap<String, Object>();
        this.observers = new LinkedHashSet<ConfigurationObserver>();
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

    public boolean addObserver(ConfigurationObserver listener) {
        return observers.add(listener);
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return get(key, defaultValue);
    }

    public Byte getByte(String key) {
        return getByte(key, null);
    }

    public Byte getByte(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.byteValue();
    }

    public Date getDate(String key) {
        return getDate(key, null);
    }

    public Date getDate(String key, Date defaultValue) {
        return get(key, defaultValue);
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public Double getDouble(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    public Float getFloat(String key) {
        return getFloat(key, null);
    }

    public Float getFloat(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.floatValue();
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Integer getInteger(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    public Set<String> getKeys() {
        return this.configs.keySet();
    }

    public <T> List<T> getList(String key) {
        return getList(key, null);
    }

    public <T> List<T> getList(String key, List<T> defaultValue) {
        return get(key, defaultValue);
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Long getLong(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.longValue();
    }

    public <T> Map<String, T> getMap(String key) {
        return getMap(key, null);
    }

    public <T> Map<String, T> getMap(String key, Map<String, T> defaultValue) {
        return get(key, defaultValue);
    }

    public Short getShort(String key) {
        return getShort(key, null);
    }

    public Short getShort(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.shortValue();
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }

    public Object remove(String key) {
        ds.getCollection(collection).remove(new BasicDBObject("_id", key));
        return configs.remove(key);
    }

    public boolean removeObserver(ConfigurationObserver listener) {
        return observers.remove(listener);
    }

    public boolean set(String key, Object value) {
        Object ovalue = configs.get(key);
        if (valuesAreEqual(ovalue, value)) {
            return false;
        }

        DBObject query = new BasicDBObject(KEY, key);
        DBObject data = new BasicDBObject(KEY, key);
        data.put(VALUE, value);
        ds.getCollection(collection).update(query, data, true, false);
        configs.put(key, value);
        changed(key, ovalue, value);
        return true;
    }

    @SuppressWarnings("unchecked")
    protected <T> T get(String key, T defaultValue) {
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

    private void changed(String key, Object ovalue, Object value) {
        for (ConfigurationObserver observer : observers) {
            observer.onConfigurationUpdate(key, ovalue, value);
        }
    }

    void reload() {
        Set<String> existing = configs.keySet();
        DBCursor allConfigs = ds.getCollection(collection, true).find();
        for (DBObject o : allConfigs) {
            String k = (String) o.get(KEY);
            Object v = o.get(VALUE);
            Object oldValue = configs.get(k);
            configs.put(k, v);
            if (!valuesAreEqual(oldValue, v)) {
                changed(k, oldValue, v);
            }
            existing.remove(k);
        }
        for (String toBeRemoved : existing) {
            Object removed = configs.remove(toBeRemoved);
            changed(toBeRemoved, removed, null);
        }
    }

    @SuppressWarnings("null")
    private boolean valuesAreEqual(Object a, Object b) {
        if (a == b) { // both are null or same memory reference
            return true;
        } else if (a == null & b != null || a != null && b == null) {
            return false;
        }
        return a.equals(b);
    }
}
