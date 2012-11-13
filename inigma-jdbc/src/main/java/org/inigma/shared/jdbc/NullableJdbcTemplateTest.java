package org.inigma.shared.jdbc;

import static org.junit.Assert.*;

import org.hsqldb.jdbc.JDBCDriver;
import org.hsqldb.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class NullableJdbcTemplateTest {
    private static Server server;
    private static JdbcTemplate template;

    @AfterClass
    public static void destroyDb() {
        server.stop();
    }

    @BeforeClass
    public static void initDb() throws Exception {
        server = DbServerUtil.startServer("template");
        DbServerUtil.executeSql("template", "/simple-table.sql");
        template = new NullableJdbcTemplate(new SimpleDriverDataSource(JDBCDriver.driverInstance,
                "jdbc:hsqldb:hsql://127.0.0.1/template"));
    }

    @Test
    public void queryWithClassType() {
        String value = template.queryForObject("SELECT name FROM test", String.class);
        assertNotNull(value);
        value = template.queryForObject("SELECT name FROM test WHERE id=13", String.class);
        assertNull(value);
    }
}
