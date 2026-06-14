package org.example.db;

import io.qameta.allure.Step;

import java.sql.Connection;
import java.sql.DriverManager;

public class SupabaseDB {

    private static Connection connection;
    private static final String URL = "jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    private static final String USER = "postgres.hbfnaltjcpuohnvfwldg";
    private static final String PASSWORD = "postgres@4972";

    @Step("Establishing Superbase database connection")
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Superbase DB connection failed", e);
        }
        return connection;
    }
}