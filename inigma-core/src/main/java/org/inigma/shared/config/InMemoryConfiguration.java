package org.inigma.shared.config;

import java.util.Map;

public class InMemoryConfiguration extends AbstractConfiguration {
    public InMemoryConfiguration() {
    }

    public InMemoryConfiguration(Map<String, Object> values) {
        setValues(values);
    }

    public void setValues(Map<String, Object> values) {
        super.reload(values);
    }

    @Override
    protected <T> T getValue(String key, Class<T> type) {
        return null;
    }

    @Override
    protected void removeValue(String key) {
    }

    @Override
    protected void setValue(String key, Object value) {
    }
}
