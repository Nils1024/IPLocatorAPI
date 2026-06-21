package de.nils.iplocatorapi.repository;

import de.nils.iplocatorapi.common.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);

    private Connection conn;

    DatabaseConnection() throws SQLException {
        String user = System.getenv("IPLocatorAPI_DB_USER");
        String passwd = System.getenv("IPLocatorAPI_DB_PASSWD");

        log.info("Connecting to database <{}> with user <{}>", Const.SQL.URL, user);

        final Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", passwd);

        conn = DriverManager.getConnection(Const.SQL.URL, properties);
    }

    public Connection getConnection() {
        return conn;
    }
}
