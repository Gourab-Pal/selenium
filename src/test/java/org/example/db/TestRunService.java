package org.example.db;

import io.qameta.allure.Step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestRunService {

    @Step("Updating test_run database with suite level details...")
    public static String createTestRun() {

        String testRunId = UUID.randomUUID().toString();

        String sql = """
            INSERT INTO public.test_run (
                id,
                project_name,
                branch,
                environment,
                triggered_by,
                status,
                total_tests,
                passed_tests,
                failed_tests,
                duration_ms,
                started_at,
                ended_at,
                created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now(), now())
        """;

        try (Connection conn = SupabaseDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, UUID.fromString(testRunId));
            stmt.setString(2, "swaglab-tests");
            stmt.setString(3, System.getenv().getOrDefault("BRANCH", "local"));
            stmt.setString(4, System.getenv().getOrDefault("ENVIRONMENT", "local_ide"));
            stmt.setString(5, System.getenv().getOrDefault("TRIGGERED_BY", "testng"));
            stmt.setString(6, "PARTIAL");
            stmt.setInt(7, 0);
            stmt.setInt(8, 0);
            stmt.setInt(9, 0);
            stmt.setLong(10, 0);

            stmt.executeUpdate();

            return testRunId;

        } catch (Exception e) {
            e.printStackTrace();   // 🔥 show real PostgreSQL error
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Step("Updating pass fail count in test_run database...")
    public static Map<String, Integer> getTestRunSummary(String testRunId) {

        String sql = """
        SELECT
            COUNT(*) AS total_tests,
            COUNT(*) FILTER (WHERE status = 'PASSED') AS passed_tests,
            COUNT(*) FILTER (WHERE status = 'FAILED') AS failed_tests
        FROM test_case_result
        WHERE test_run_id = ?
    """;

        try (Connection conn = SupabaseDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, UUID.fromString(testRunId));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, Integer> map = new HashMap<>();
                map.put("total", rs.getInt("total_tests"));
                map.put("passed", rs.getInt("passed_tests"));
                map.put("failed", rs.getInt("failed_tests"));
                return map;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch test summary", e);
        }

        return Map.of("total", 0, "passed", 0, "failed", 0);
    }
}