package org.inigma.shared.jdbc;

public class DataSourceConfig {
    private String driver;
    private String url;
    private String username;
    private String password;
    private int minSize;
    private int maxSize;
    private String testQuery;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataSourceConfig) {
            DataSourceConfig dsc = (DataSourceConfig) obj;
            return equal(driver, dsc.driver) && equal(url, dsc.url) && equal(username, dsc.username)
                    && equal(password, dsc.password) && minSize == dsc.minSize && maxSize == dsc.maxSize
                    && equal(testQuery, dsc.testQuery);
        }
        return false;
    }

    public String getDriver() {
        return driver;
    }

    public int getMaxSize() {
        if (maxSize <= 0) {
            return Integer.MAX_VALUE;
        }
        return maxSize;
    }

    public int getMinSize() {
        if (minSize < 0) {
            return 0;
        } else if (minSize > maxSize) {
            return maxSize;
        }
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

    @Override
    public int hashCode() {
        if (url == null) {
            return 0;
        }
        return url.hashCode();
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

    @SuppressWarnings("static-method")
    private boolean equal(Object source, Object other) {
        if (source == null && other == null) {
            return true;
        } else if (source != null) {
            return source.equals(other);
        }
        return false;
    }
}
