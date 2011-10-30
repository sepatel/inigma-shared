package org.inigma.shared.mongo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.mongodb.DBObject;

/**
 * Wrapper to the mongo DBObject class to make it easier to retrieve data in the format desired.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class DBObjectWrapper implements DBObject {
    private final DBObject ref;

    public DBObjectWrapper(DBObject ref) {
        this.ref = ref;
    }

    @Override
    public boolean containsField(String s) {
        return ref.containsField(s);
    }

    @Override
    public boolean containsKey(String s) {
        return ref.containsKey(s);
    }

    @Override
    public Object get(String key) {
        return ref.get(key);
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return getWrapper(key, defaultValue);
    }

    public Byte getByte(String key) {
        return getByte(key, null);
    }

    public Byte getByte(String key, Number defaultValue) {
        Number value = getWrapper(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.byteValue();
    }

    public Date getDate(String key) {
        return getDate(key, null);
    }

    public Date getDate(String key, Date defaultValue) {
        return getWrapper(key, defaultValue);
    }

    public DBObjectWrapper getDocument(String key) {
        return new DBObjectWrapper((DBObject) getWrapper(key, null));
    }

    public DBObjectWrapper getDocument(String key, DBObject defaultValue) {
        return getWrapper(key, new DBObjectWrapper(defaultValue));
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public Double getDouble(String key, Number defaultValue) {
        Number value = getWrapper(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    public Float getFloat(String key) {
        return getFloat(key, null);
    }

    public Float getFloat(String key, Number defaultValue) {
        Number value = getWrapper(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.floatValue();
    }

    public ObjectId getId() {
        return getId("_id");
    }

    public ObjectId getId(String key) {
        return getWrapper(key, null);
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Integer getInteger(String key, Number defaultValue) {
        Number value = getWrapper(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    public <T> List<T> getList(String key) {
        return getList(key, null);
    }

    public <T> List<T> getList(String key, List<T> defaultValue) {
        return getWrapper(key, defaultValue);
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Long getLong(String key, Number defaultValue) {
        Number value = getWrapper(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.longValue();
    }

    public <T> Map<String, T> getMap(String key) {
        return getMap(key, null);
    }

    public <T> Map<String, T> getMap(String key, Map<String, T> defaultValue) {
        return getWrapper(key, defaultValue);
    }

    public Short getShort(String key) {
        return getShort(key, null);
    }

    public Short getShort(String key, Number defaultValue) {
        Number value = getWrapper(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.shortValue();
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        return getWrapper(key, defaultValue);
    }

    @Override
    public boolean isPartialObject() {
        return ref.isPartialObject();
    }

    @Override
    public Set<String> keySet() {
        return ref.keySet();
    }

    @Override
    public void markAsPartialObject() {
        ref.markAsPartialObject();
    }

    @Override
    public Object put(String key, Object v) {
        return ref.put(key, v);
    }

    @Override
    public void putAll(BSONObject o) {
        ref.putAll(o);
    }

    @Override
    public void putAll(Map m) {
        ref.putAll(m);
    }

    @Override
    public Object removeField(String key) {
        return ref.removeField(key);
    }

    @Override
    public Map toMap() {
        return ref.toMap();
    }

    private <T> T getWrapper(String key, T defaultValue) {
        Object value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return (T) value;
    }
}
