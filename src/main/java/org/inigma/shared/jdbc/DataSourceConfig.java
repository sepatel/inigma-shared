package org.inigma.shared.jdbc;

public class DataSourceConfig {
    private String driver;
    private String url;
    private String username;
    private String password;
    private int minSize;
    private int maxSize;
    private String testQuery;

    public String getDriver() {
        return driver;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public String getPassword() {
        return password;
    }

    public String getTestQuery() {
        return testQuery;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
