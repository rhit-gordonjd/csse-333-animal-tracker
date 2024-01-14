package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database implements AutoCloseable {
    private static final String urlTemplate = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${dbUser};password={${dbPassword}};encrypt=false";
    private Connection connection;

    public Database(Config config) throws SQLException {
        String url = config.fillTemplate(urlTemplate);
        connection = DriverManager.getConnection(url);
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
