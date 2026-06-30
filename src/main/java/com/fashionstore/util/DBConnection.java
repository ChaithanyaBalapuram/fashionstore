package com.fashionstore.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = System.getenv("DB_URL");
    private static final String USERNAME = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            if (URL == null || USERNAME == null || PASSWORD == null) {
                throw new RuntimeException("Database environment variables are not set.");
            }

            System.out.println("URL: " + URL);
            System.out.println("Username: " + USERNAME);

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            System.out.println("Password exists: " + (PASSWORD != null));
            System.out.println("Password length: " + (PASSWORD == null ? 0 : PASSWORD.length()));

            System.out.println("Database Connected Successfully!");

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}