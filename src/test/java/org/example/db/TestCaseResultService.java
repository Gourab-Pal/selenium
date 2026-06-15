package org.example.db;

import io.qameta.allure.Step;
import org.example.utils.AllureLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class TestCaseResultService {

    @Step("Updating test_case_result database...")
    public static void insertTestCaseResult(
            UUID testRunId,
            String testClass,
            String testMethod,
            String status,
            long durationMs,
            String errorMessage,
            String stackTrace,
            String screenshotUrl,
            String browser,
            String environment,
            long startedAt,
            long endedAt
    ) {

        String sql = """
            INSERT INTO test_case_result (
                id,
                test_run_id,
                test_class,
                test_method,
                status,
                duration_ms,
                error_message,
                stack_trace,
                screenshot_url,
                browser,
                environment,
                started_at,
                ended_at,
                created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())
        """;

        try (Connection conn = SupabaseDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, UUID.randomUUID());
            stmt.setObject(2, testRunId);
            stmt.setString(3, testClass);
            stmt.setString(4, testMethod);
            stmt.setString(5, status);
            stmt.setLong(6, durationMs);
            stmt.setString(7, errorMessage);
            stmt.setString(8, stackTrace);
            stmt.setString(9, screenshotUrl);
            stmt.setString(10, browser);
            stmt.setString(11, environment);
            stmt.setTimestamp(12, new java.sql.Timestamp(startedAt));
            stmt.setTimestamp(13, new java.sql.Timestamp(endedAt));

            stmt.executeUpdate();
            AllureLogger.log("test_case_result db insert is done");

        } catch (Exception e) {
            throw new RuntimeException("Failed to insert test case result", e);
        }
    }
}