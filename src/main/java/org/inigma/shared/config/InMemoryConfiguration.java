package org.inigma.shared.config;

public class InMemoryConfiguration extends AbstractConfiguration {
    @Override
    protected void setValue(String key, Object value) {
    }

    @Override
    protected void removeValue(String key) {
    }

    @Override
    protected <T> T getValue(String key, Class<T> type) {
        return null;
    }
}
