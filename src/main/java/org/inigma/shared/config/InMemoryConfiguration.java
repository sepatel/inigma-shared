package org.inigma.shared.config;

public class InMemoryConfiguration extends AbstractConfiguration {
    @Override
    protected void setValue(String key, Object value) {
    }

    @Override
    protected Object getValue(String key) {
        return null;
    }
}
