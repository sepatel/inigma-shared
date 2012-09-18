package org.inigma.shared.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.inigma.shared.config.Configuration;
import org.inigma.shared.config.ConfigurationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class DynamicDataSource implements DataSource, ConfigurationObserver {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private BoneCPDataSource ds;
    @Autowired
    private Configuration config;
    private String configKey;

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
        if (configKey.equals(key)) {
            initialize();
        }
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        ds.setLoginTimeout(seconds);
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        ds.setLogWriter(out);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not yet investigated!");
    }

    @PreDestroy
    public void close() {
        ds.close();
    }

    @PostConstruct
    private void initialize() {
        BoneCPDataSource oldds = ds;
        DataSourceConfig dsc = config.get(configKey, null, DataSourceConfig.class);

        if (dsc != null) {
            logger.info("Initializing datasource using url '{}' and user '{}'", dsc.getUrl(), dsc.getUsername());
            try {
                Class.forName(dsc.getDriver()); // makes sure it is in the classpath
                BoneCPConfig bcp = new BoneCPConfig();
                bcp.setJdbcUrl(dsc.getUrl());
                bcp.setUsername(dsc.getUsername());
                bcp.setPassword(dsc.getPassword());
                bcp.setMinConnectionsPerPartition(dsc.getMinSize());
                bcp.setMaxConnectionsPerPartition(dsc.getMaxSize());
                if (dsc.getTestQuery() != null) {
                    bcp.setConnectionTestStatement(dsc.getTestQuery());
                }
                ds = new BoneCPDataSource(bcp);
                if (oldds != null) {
                    oldds.close();
                }
            } catch (Exception e) {
                logger.error("Auto-generated error log, Cannot initialize data source", e);
            }
        } else {
            logger.warn("Unable to initialize datasource. Configurations likely incorrect!");
        }
        config.addObserver(this);
    }
}
