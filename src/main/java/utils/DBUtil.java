package utils;

import config.Config;

import java.sql.*;
import java.util.Properties;

public class DBUtil {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            props.setProperty("user", Config.getDbUser());
            props.setProperty("password", Config.getDbPassword());
            connection = DriverManager.getConnection(Config.getDbUrl(), props);
        }
        return connection;
    }

    public static String executeQuery(String query) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        }
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}