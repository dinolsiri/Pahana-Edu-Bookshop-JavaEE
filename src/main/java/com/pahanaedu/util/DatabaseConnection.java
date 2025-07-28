package com.pahanaedu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {

    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties");
                props.load(input);

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");
                String driver = props.getProperty("db.driver");

                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
