package Util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class for creating database connections
 */
public class DBConnection {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "ajay123";

    /**
     * Creates and returns a new database connection
     */
    public static Connection getConnection() {

        Connection connection = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println("❌ Database Connection Error: " + e.getMessage());
        }

        return connection;
    }
}
