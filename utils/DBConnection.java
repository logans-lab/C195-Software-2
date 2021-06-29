package software2.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection class. Methods to start, get, and close connection. Includes auto-reconnect for extended use
 * of application.
 * @author logan
 */
public class DBConnection {

    private static final String protocol = "jdbc:";
    private static final String vendorName = "mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ0624m";
    private static final String autoReconnect = "?autoReconnect=true";
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName + autoReconnect;

    private static final String mySQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection connection = null;

    private static final String username = "U0624m";
    private static final String password = "53688672547";

    /**
     * Start connection. Connects application to related database.
     * @return Connection object used in Main.java.
     */
    public static Connection startConnection() {

        try {
            Class.forName(mySQLJDBCDriver);
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Get connection.
     * @return Connection object used database queries throughout application.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Close connection. Closes application to related database.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch (Exception e) {
            // do nothing
        }
    }
}
