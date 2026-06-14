package org.example.db;

import io.qameta.allure.Step;

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
                browser,
                environment,
                started_at,
                ended_at,
                created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())
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
            stmt.setString(9, browser);
            stmt.setString(10, environment);
            stmt.setTimestamp(11, new java.sql.Timestamp(startedAt));
            stmt.setTimestamp(12, new java.sql.Timestamp(endedAt));

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to insert test case result", e);
        }
    }
}