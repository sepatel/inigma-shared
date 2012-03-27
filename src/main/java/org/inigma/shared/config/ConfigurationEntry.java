package org.inigma.shared.config;

public class ConfigurationEntry {
    private String id;
    private Object value;

    public ConfigurationEntry() {
    }

    public ConfigurationEntry(String id, Object value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
