package org.example.db;

import io.qameta.allure.Step;
import org.example.config.TestConfig;
import org.example.utils.AllureLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestRunService {

    @Step("Updating database with suite level details...")
    public static String createTestRun() {

        String testRunId = UUID.randomUUID().toString();

        String sql = """
            INSERT INTO %s (
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
        """.formatted(TestConfig.getTestRunTableName());

        try (Connection conn = SupabaseDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, UUID.fromString(testRunId));
            stmt.setString(2, TestConfig.getProjectName());
            stmt.setString(3, System.getenv().getOrDefault("BRANCH", TestConfig.getLocalBranch()));
            stmt.setString(4, System.getenv().getOrDefault("ENVIRONMENT", TestConfig.getLocalEnv()));
            stmt.setString(5, System.getenv().getOrDefault("TRIGGERED_BY", TestConfig.getLocalTrigger()));
            stmt.setString(6, TestConfig.getPartialStatus());
            stmt.setInt(7, 0);
            stmt.setInt(8, 0);
            stmt.setInt(9, 0);
            stmt.setLong(10, 0);

            stmt.executeUpdate();
            AllureLogger.log("Entries inserted into db table with suite level details");

            return testRunId;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Step("Updating pass fail count in suite level table database...")
    public static Map<String, Integer> getTestRunSummary(String testRunId) {

        String sql = """
        SELECT
            COUNT(*) AS total_tests,
            COUNT(*) FILTER (WHERE status = 'PASSED') AS passed_tests,
            COUNT(*) FILTER (WHERE status = 'FAILED') AS failed_tests
        FROM %s
        WHERE test_run_id = ?
    """.formatted(TestConfig.getTestCaseResultTableName());

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

        AllureLogger.log("Entries updated into suite level table");

        return Map.of("total", 0, "passed", 0, "failed", 0);
    }
}