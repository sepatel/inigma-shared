package org.inigma.shared.webapp;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MapRestResponse<K, V> implements RestResponse, Map<K, V> {
    private Map<K, V> ref;

    public MapRestResponse(Map<K, V> source) {
        this.ref = source;
    }

    @Override
    public void clear() {
        ref.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return ref.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return ref.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return ref.entrySet();
    }

    @Override
    public V get(Object key) {
        return ref.get(key);
    }

    @Override
    public boolean isEmpty() {
        return ref.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return ref.keySet();
    }

    @Override
    public V put(K key, V value) {
        return ref.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        ref.putAll(m);
    }

    @Override
    public V remove(Object key) {
        return ref.remove(key);
    }

    @Override
    public int size() {
        return ref.size();
    }

    @Override
    public Collection<V> values() {
        return ref.values();
    }
}
