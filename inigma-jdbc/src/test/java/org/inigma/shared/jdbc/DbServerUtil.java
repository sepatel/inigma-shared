package org.inigma.shared.jdbc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;

import org.hsqldb.jdbc.JDBCDriver;
import org.hsqldb.server.Server;
import org.springframework.util.StringUtils;

final class DbServerUtil {
    public static Server startServer(String... instances) {
        Server server = new Server();
        server.setDaemon(true);
        for (int i = 0; i < instances.length; i++) {
            server.setDatabaseName(i, instances[i]);
            server.setDatabasePath(i, "file:target/" + instances[i] + "." + UUID.randomUUID().toString());
        }
        server.start();
        return server;
    }
    
    public static void executeSql(String instance, String resource) throws Exception {
        executeSql(JDBCDriver.getConnection("jdbc:hsqldb:hsql://127.0.0.1/" + instance, null), resource);
    }

    public static void executeSql(Connection conn, String resource) throws Exception {
        InputStream in = DbServerUtil.class.getResourceAsStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Statement stmt = conn.createStatement();
        try {
            String sql;
            while ((sql = reader.readLine()) != null) {
                if (StringUtils.hasText(sql)) {
                    stmt.execute(sql);
                }
            }
        } finally {
            stmt.close();
        }
    }
}
