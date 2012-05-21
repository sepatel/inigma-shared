package org.inigma.shared.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.inigma.shared.config.Configuration;
import org.inigma.shared.config.ConfigurationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class DynamicDataSource implements DataSource, ConfigurationObserver {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private DataSource ds;
    @Autowired
    private Configuration config;
    private String driverKey;
    private String urlKey;
    private String usernameKey;
    private String passwordKey;

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return ds.getConnection(username, password);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return ds.getLoginTimeout();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return ds.getLogWriter();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return ds.isWrapperFor(iface);
    }

    @Override
    public void onConfigurationUpdate(String key, Object original, Object current) {
        if ("db_url".equals(key) || "db_user".equals(key) || "db_pass".equals(key) || "db_driver".equals(key)) {
            // TODO: Reconfigure the datasource and close out the existing one.
        }
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setDriverKey(String driverKey) {
        this.driverKey = driverKey;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        ds.setLoginTimeout(seconds);
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        ds.setLogWriter(out);
    }

    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return ds.unwrap(iface);
    }

    @PostConstruct
    @SuppressWarnings("unused")
    private void initialize() {
        String driver = config.getString(driverKey, null);
        String url = config.getString(urlKey, null);
        String user = config.getString(usernameKey, null);
        String pass = config.getString(passwordKey, null);

        logger.info("Initializing datasource using url '{}' and user '{}'", url, user);
        if (url != null) {
            try {
                ds = new SimpleDriverDataSource((Driver) Class.forName(driver).newInstance(), url, user, pass);
            } catch (Exception e) {
                logger.error("Auto-generated error log, Cannot initialize data source", e);
            }
        } else {
            logger.warn("Unable to initialize datasource. Configurations likely incorrect!");
        }
    }
}
