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

    /**
     * @see org.inigma.lwrest.config.Configuration#addObserver(org.inigma.lwrest.config.ConfigurationObserver)
     */
    @Override
    public boolean addObserver(ConfigurationObserver listener) {
        return observers.add(listener);
    }

    private void changed(String key, Object ovalue, Object value) {
        for (ConfigurationObserver observer : observers) {
            observer.onConfigurationUpdate(key, ovalue, value);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T get(String key) {
        if (!configs.containsKey(key)) { // load configuration into the cache if
                                         // missing
            Object value = getValue(key);
            if (value == null) {
                throw new IllegalStateException("Configuration " + key + " not found!");
            }
            configs.put(key, value);
        }
        return (T) configs.get(key);
    }

    @SuppressWarnings("unchecked")
    protected <T> T get(String key, T defaultValue) {
        if (!configs.containsKey(key)) { // load configuration into the cache if
                                         // missing
            Object value = getValue(key);
            if (value == null) {
                return defaultValue;
            }
            configs.put(key, value);
        }
        return (T) configs.get(key);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getBoolean(java.lang.String)
     */
    @Override
    public Boolean getBoolean(String key) {
        return get(key);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getBoolean(java.lang.String, java.lang.Boolean)
     */
    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return get(key, defaultValue);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getByte(java.lang.String)
     */
    @Override
    public Byte getByte(String key) {
        Number value = get(key);
        if (value == null) {
            return null;
        }
        return value.byteValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getByte(java.lang.String, java.lang.Number)
     */
    @Override
    public Byte getByte(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.byteValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getDate(java.lang.String)
     */
    @Override
    public Date getDate(String key) {
        return get(key);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getDate(java.lang.String, java.util.Date)
     */
    @Override
    public Date getDate(String key, Date defaultValue) {
        return get(key, defaultValue);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getDouble(java.lang.String)
     */
    @Override
    public Double getDouble(String key) {
        Number value = get(key);
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getDouble(java.lang.String, java.lang.Number)
     */
    @Override
    public Double getDouble(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getFloat(java.lang.String)
     */
    @Override
    public Float getFloat(String key) {
        Number value = get(key);
        if (value == null) {
            return null;
        }
        return value.floatValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getFloat(java.lang.String, java.lang.Number)
     */
    @Override
    public Float getFloat(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.floatValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getInteger(java.lang.String)
     */
    @Override
    public Integer getInteger(String key) {
        Number value = get(key);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getInteger(java.lang.String, java.lang.Number)
     */
    @Override
    public Integer getInteger(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getKeys()
     */
    @Override
    public Set<String> getKeys() {
        return this.configs.keySet();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getList(java.lang.String)
     */
    @Override
    public <T> List<T> getList(String key) {
        return get(key);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getList(java.lang.String, java.util.List)
     */
    @Override
    public <T> List<T> getList(String key, List<T> defaultValue) {
        return get(key, defaultValue);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getLong(java.lang.String)
     */
    @Override
    public Long getLong(String key) {
        Number value = get(key);
        if (value == null) {
            return null;
        }
        return value.longValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getLong(java.lang.String, java.lang.Number)
     */
    @Override
    public Long getLong(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.longValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getMap(java.lang.String)
     */
    @Override
    public <T> Map<String, T> getMap(String key) {
        return get(key);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getMap(java.lang.String, java.util.Map)
     */
    @Override
    public <T> Map<String, T> getMap(String key, Map<String, T> defaultValue) {
        return get(key, defaultValue);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getShort(java.lang.String)
     */
    @Override
    public Short getShort(String key) {
        Number value = get(key);
        if (value == null) {
            return null;
        }
        return value.shortValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getShort(java.lang.String, java.lang.Number)
     */
    @Override
    public Short getShort(String key, Number defaultValue) {
        Number value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        return value.shortValue();
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getString(java.lang.String)
     */
    @Override
    public String getString(String key) {
        return get(key);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#getString(java.lang.String, java.lang.String)
     */
    @Override
    public String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }

    protected abstract Object getValue(String key);

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

    /**
     * @see org.inigma.lwrest.config.Configuration#remove(java.lang.String)
     */
    @Override
    public Object remove(String key) {
        return configs.remove(key);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#removeObserver(org.inigma.lwrest.config.ConfigurationObserver)
     */
    @Override
    public boolean removeObserver(ConfigurationObserver listener) {
        return observers.remove(listener);
    }

    /**
     * @see org.inigma.lwrest.config.Configuration#set(java.lang.String, java.lang.Object)
     */
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

    protected abstract void setValue(String key, Object value);

    private boolean valuesAreEqual(Object a, Object b) {
        if (a == b) { // both are null or same memory reference
            return true;
        } else if (a == null & b != null || a != null && b == null) {
            return false;
        }
        return a.equals(b);
    }
}
