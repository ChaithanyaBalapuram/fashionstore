package com.fashionstore.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        URL = props.getProperty("db.url", "DB_URL");
        USERNAME = props.getProperty("db.username", "DB_USER");
        PASSWORD = props.getProperty("db.password", "DB_PASSWORD");
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
