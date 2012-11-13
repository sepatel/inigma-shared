package org.inigma.shared.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.jdbc.JDBCDriver;
import org.hsqldb.server.Server;
import org.inigma.shared.config.Configuration;
import org.inigma.shared.config.InMemoryConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DynamicDataSourceIT {
    private static Server server;

    @AfterClass
    public static void destroyDb() {
        server.stop();
    }

    @BeforeClass
    public static void initDb() throws Exception {
        server = DbServerUtil.startServer("test");
        DbServerUtil.executeSql("test", "/simple-table.sql");
    }

    @Test
    public void getConnection() throws SQLException {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriver(JDBCDriver.class.getName());
        dsc.setUrl("jdbc:hsqldb:hsql://127.0.0.1/test");
        dsc.setUsername("SA");

        Configuration config = new InMemoryConfiguration();
        config.set("db", dsc);

        DynamicDataSource ds = new DynamicDataSource();
        ds.setConfigKey("db");
        ds.setConfig(config);
        ds.initialize();

        Connection connection = ds.getConnection();
        assertNotNull(connection);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name FROM test");
        assertTrue(rs.next());
        assertFalse(rs.next());
        rs.close();
        stmt.close();
        connection.close();
    }
}
