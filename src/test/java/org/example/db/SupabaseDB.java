package org.example.db;

import io.qameta.allure.Step;
import org.example.config.TestConfig;
import org.example.utils.AllureLogger;
import java.sql.Connection;
import java.sql.DriverManager;

public class SupabaseDB {
    //private static Connection connection;
    private static final String URL = System.getenv("SUPABASE_DB_URL");
    private static final String USER = System.getenv("SUPABASE_DB_USER");
    private static final String PASSWORD = System.getenv("SUPABASE_DB_PASSWORD");

    @Step("Establishing Supabase database connection")
    public static Connection getConnection() {
        try {
            Class.forName(TestConfig.getJdbcDriver());
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            AllureLogger.log("Supabase DB connected");
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Supabase DB connection failed", e);
        }
    }
}