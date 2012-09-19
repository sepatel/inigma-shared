package org.inigma.shared.config;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public abstract class AbstractConfiguration implements Configuration {
    private final Map<String, Object> configs = new HashMap<String, Object>();
    private final Set<ConfigurationObserver> observers = new HashSet<ConfigurationObserver>();

    @Override
    public boolean addObserver(ConfigurationObserver listener) {
        return observers.add(listener);
    }

    @Override
    public Object get(String key) {
        return get(key, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        if (!configs.containsKey(key)) { // load configuration into the cache if missing
            T value = getValue(key, type);
            if (value == null) {
                throw new IllegalStateException("Configuration " + key + " not found!");
            }
            configs.put(key, value);
        }
        return (T) configs.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, T defaultValue, Class<T> type) {
        if (!configs.containsKey(key)) { // load configuration into the cache if missing
            Object value = getValue(key, type);
            if (value == null) {
                return defaultValue;
            }
            configs.put(key, value);
        }
        return (T) configs.get(key);
    }

    @Override
    public Boolean getBoolean(String key) {
        return get(key, Boolean.class);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return get(key, defaultValue, Boolean.class);
    }

    @Override
    public Byte getByte(String key) {
        Number value = get(key, Byte.class);
        if (value == null) {
            return null;
        }
        return value.byteValue();
    }

    @Override
    public Byte getByte(String key, Number defaultValue) {
        Number value = get(key, defaultValue, Number.class);
        if (value == null) {
            return null;
        }
        return value.byteValue();
    }

    @Override
    public Date getDate(String key) {
        return get(key, Date.class);
    }

    @Override
    public Date getDate(String key, Date defaultValue) {
        return get(key, defaultValue, Date.class);
    }

    @Override
    public Double getDouble(String key) {
        Number value = get(key, Number.class);
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    @Override
    public Double getDouble(String key, Number defaultValue) {
        Number value = get(key, defaultValue, Number.class);
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    @Override
    public Float getFloat(String key) {
        Number value = get(key, Number.class);
        if (value == null) {
            return null;
        }
        return value.floatValue();
    }

    @Override
    public Float getFloat(String key, Number defaultValue) {
        Number value = get(key, defaultValue, Number.class);
        if (value == null) {
            return null;
        }
        return value.floatValue();
    }

    @Override
    public Integer getInteger(String key) {
        Number value = get(key, Number.class);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    @Override
    public Integer getInteger(String key, Number defaultValue) {
        Number value = get(key, defaultValue, Number.class);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    @Override
    public Set<String> getKeys() {
        return this.configs.keySet();
    }

    @Override
    public <T> List<T> getList(String key) {
        return get(key, List.class);
    }

    @Override
    public <T> List<T> getList(String key, List<T> defaultValue) {
        return get(key, defaultValue, List.class);
    }

    @Override
    public Long getLong(String key) {
        Number value = get(key, Number.class);
        if (value == null) {
            return null;
        }
        return value.longValue();
    }

    @Override
    public Long getLong(String key, Number defaultValue) {
        Number value = get(key, defaultValue, Number.class);
        if (value == null) {
            return null;
        }
        return value.longValue();
    }

    @Override
    public <T> Map<String, T> getMap(String key) {
        return get(key, Map.class);
    }

    @Override
    public <T> Map<String, T> getMap(String key, Map<String, T> defaultValue) {
        return get(key, defaultValue, Map.class);
    }

    @Override
    public Short getShort(String key) {
        Number value = get(key, Number.class);
        if (value == null) {
            return null;
        }
        return value.shortValue();
    }

    @Override
    public Short getShort(String key, Number defaultValue) {
        Number value = get(key, defaultValue, Number.class);
        if (value == null) {
            return null;
        }
        return value.shortValue();
    }

    @Override
    public String getString(String key) {
        return get(key, String.class);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return get(key, defaultValue, String.class);
    }

    @Override
    public Object remove(String key) {
        removeValue(key);
        return configs.remove(key);
    }

    @Override
    public boolean removeObserver(ConfigurationObserver listener) {
        return observers.remove(listener);
    }

    @Override
    public boolean set(String key, Object value) {
        Object ovalue = configs.get(key);
        if (valuesAreEqual(ovalue, value)) {
            return false;
        }
        setValue(key, value);
        configs.put(key, value);
        changed(key, ovalue, value);
        return true;
    }

    protected abstract <T> T getValue(String key, Class<T> type);

    protected final void reload(Map<String, Object> newconfigs) {
        Set<String> existing = new HashSet<String>(configs.keySet());
        for (Entry<String, Object> entries : newconfigs.entrySet()) {
            String k = entries.getKey();
            Object v = entries.getValue();
            Object oldValue = configs.get(k);
            if (!configs.containsKey(k) || !valuesAreEqual(oldValue, v)) {
                configs.put(k, v);
                changed(k, oldValue, v);
            }
            existing.remove(k);
        }
        for (String toBeRemoved : existing) {
            Object removed = configs.remove(toBeRemoved);
            changed(toBeRemoved, removed, null);
        }
    }

    protected abstract void removeValue(String key);

    protected abstract void setValue(String key, Object value);

    private void changed(String key, Object ovalue, Object value) {
        for (ConfigurationObserver observer : observers) {
            observer.onConfigurationUpdate(key, ovalue, value);
        }
    }

    private boolean valuesAreEqual(Object a, Object b) {
        if (a == b) { // both are null or same memory reference
            return true;
        } else if (a == null && b != null || a != null && b == null) {
            return false;
        }
        return a.equals(b);
    }
}
