package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TestResultRepository {

    private static final String INSERT_SQL =
            "INSERT INTO swaglab_results (test_name, status, browser) VALUES (?, ?, ?)";

    public static void saveResult(String testName, String status, String browser) {
        System.out.println("DB INSERT CALLED: " + testName + " | " + status + " | " + browser);

        try (Connection conn = DriverManager.getConnection(
                DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setString(1, testName);
            stmt.setString(2, status);
            stmt.setString(3, browser);

            int rows = stmt.executeUpdate();
            System.out.println("Rows inserted: " + rows);

        } catch (Exception e) {
            System.out.println("DB INSERT FAILED");
            e.printStackTrace();
        }
    }
}