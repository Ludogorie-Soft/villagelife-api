package com.example.ludogorieSoft.village.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/village";
    private static final String username = "root";
    private static final String password = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, username, password);
    }
}
