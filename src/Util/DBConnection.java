package Util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static Connection con;

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "ajay123";

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (Exception e) {
            System.out.println("❌ DB Connection Failed: " + e.getMessage());
        }
        return con;
    }

}
