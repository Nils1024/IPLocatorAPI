package de.nils.iplocatorapi.repository;

import de.nils.iplocatorapi.common.Const;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private Connection conn;

    DatabaseConnection() throws SQLException {
        final Properties properties = new Properties();
        properties.setProperty("user", System.getenv("IPLocatorAPI_DB_USER"));
        properties.setProperty("password", System.getenv("IPLocatorAPI_DB_PASSWD"));

        conn = DriverManager.getConnection(Const.SQL.url, properties);
    }
}
