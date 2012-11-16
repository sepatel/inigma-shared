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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DynamicDataSourceIT {
    private static Server server;
    private DynamicDataSource ds;

    @AfterClass
    public static void destroyDb() {
        server.stop();
    }

    @BeforeClass
    public static void initDb() throws Exception {
        server = DbServerUtil.startServer("test");
        DbServerUtil.executeSql("test", "/simple-table.sql");
    }

    @Before
    public void setup() throws Exception {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriver(JDBCDriver.class.getName());
        dsc.setUrl("jdbc:hsqldb:hsql://127.0.0.1/test");
        dsc.setUsername("SA");
        dsc.setTestQuery("SELECT name FROM test");

        Configuration config = new InMemoryConfiguration();
        config.set("db", dsc);

        ds = new DynamicDataSource();
        ds.setConfigKey("db");
        ds.setConfig(config);
        ds.initialize();
    }

    @Test
    public void getConnection() throws SQLException {
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

    @Test
    public void getConnectionAfterDamagedNetwork() throws Exception {
        getConnection();
        server.stop();
        Thread.sleep(3000); // give a little time for the ds to properly be broken :)
        server.start();
        try {
            getConnection();
            fail("Would be nice if it did not fail here but since it is on the sql execution it makes sense. Nicer would be to fail on getConnection instead ...");
        } catch (SQLException e) {
            // ok if it happens once as long as it recovers properly.
        }
        getConnection();
    }
}
