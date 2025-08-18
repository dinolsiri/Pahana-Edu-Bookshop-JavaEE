package com.icbt.cis6003.pahanaedubookshop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pahana_edu_bookshop";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("MySQL JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found: " + e.getMessage());
            throw new RuntimeException("Database driver not found", e);
        }
    }


    /**
     * Get a database connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                                         DB_USERNAME, DB_PASSWORD);
    }

    /**
     * Close database connection safely
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
