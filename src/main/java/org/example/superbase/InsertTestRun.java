package org.example.superbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertTestRun {

    public static void main(String[] args) {

        String url =
                "jdbc:postgresql://db.hbfnaltjcpuohnvfwldg.supabase.co:5432/postgres?sslmode=require";

        String user = "postgres";
        String password = "postgres@4972"; // <-- replace this

        String insertSql = """
                INSERT INTO test_run (
                    project_name,
                    branch,
                    environment,
                    triggered_by,
                    status,
                    total_tests,
                    passed_tests,
                    failed_tests,
                    duration_ms
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id;
                """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {

            stmt.setString(1, "swaglab-tests");
            stmt.setString(2, "local");
            stmt.setString(3, "local");
            stmt.setString(4, "manual-java");
            stmt.setString(5, "PASSED");
            stmt.setInt(6, 10);
            stmt.setInt(7, 10);
            stmt.setInt(8, 0);
            stmt.setLong(9, 12345);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Inserted test_run ID: " + rs.getString("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}