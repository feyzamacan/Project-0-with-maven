package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class Connect {

    private static Connection connection;

    private Connect() {
        // prevent instantiation
    }

    public static Connection getConnection() {

        try {
            // Reuse connection if valid
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            Properties props = new Properties();

            try (InputStream input =
                         Connect.class.getClassLoader()
                                 .getResourceAsStream("Connection.properties")) {

                if (input == null) {
                    throw new RuntimeException("Connection.properties not found in classpath");
                }

                props.load(input);
            }

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            if (url == null || username == null || password == null) {
                throw new RuntimeException("Missing database configuration values");
            }

            connection = DriverManager.getConnection(url, username, password);
            return connection;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load Connection.properties", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }
}
