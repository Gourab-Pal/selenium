package org.example.core;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.example.config.TestConfig;
import org.example.db.TestCaseResultService;
import org.example.db.TestRunContext;
import org.example.db.TestRunService;
import org.example.db.SupabaseDB;
import org.example.listeners.RetryAnalyzer;
import org.example.utils.AllureLogger;
import org.example.utils.BaseTestUtils;
import org.example.utils.BrowserVersionUtils;
import org.example.utils.ScreenshotUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public abstract class BaseTest {
    protected WebDriver driver;
    private long suiteStartTime;
    private long testStartTime;

    @BeforeSuite
    public void beforeSuite() {
        suiteStartTime = System.currentTimeMillis();
        String runId = TestRunService.createTestRun();
        TestRunContext.setTestRunId(runId);
        AllureLogger.log("Test Run Created: " + runId);
    }

    @Step("Setup Driver")
    @BeforeMethod
    public void setUpDriver(ITestResult result) {
        testStartTime = System.currentTimeMillis();
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
    }

    @Step("Teardown & Log Result")
    @AfterMethod
    public void tearDownDriver(ITestResult result) {
        if (result.wasRetried()) {
            DriverManager.quitDriver();
            return;
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - testStartTime;
        String status = BaseTestUtils.getTestCaseStatus(result);
        ArrayList<String> errorDetails = BaseTestUtils.getErrorDetails(result);
        String errorMessage = errorDetails.get(0);
        String stackTrace = errorDetails.get(1);
        String screenshotPath = BaseTestUtils.getScreenshotPath(driver, result);
        int retryCount = RetryAnalyzer.getRetryCount(result);
        BaseTestUtils.insertTestCaseResultIntoDatabase(result, duration, testStartTime, endTime, retryCount, status, errorMessage, stackTrace, screenshotPath);
        if (driver != null) {DriverManager.quitDriver();}
    }

    @AfterSuite
    public void updateTestRunSummary() {
        long suiteEndTime = System.currentTimeMillis();
        long duration = suiteEndTime - suiteStartTime;
        BaseTestUtils.updateTestRunDatabase(duration);
//        String runId = TestRunContext.getTestRunId();
//        Map<String, Integer> summary = TestRunService.getTestRunSummary(runId);
//        int total = summary.get("total");
//        int passed = summary.get("passed");
//        int failed = summary.get("failed");
//        String finalStatus = (failed > 0) ? "FAILED" : "PASSED";
//        String sql = """
//        UPDATE public.test_run
//        SET total_tests = ?,
//            passed_tests = ?,
//            failed_tests = ?,
//            duration_ms = ?,
//            status = ?
//        WHERE id = ?
//        """;
//
//        try (Connection conn = SupabaseDB.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, total);
//            stmt.setInt(2, passed);
//            stmt.setInt(3, failed);
//            stmt.setLong(4, duration);
//            stmt.setString(5, finalStatus);
//            stmt.setObject(6, UUID.fromString(runId));
//
//            stmt.executeUpdate();
//            AllureLogger.log("Test Run Updated Successfully");
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to update test run summary: " + e.getMessage());
//        }
    }
}
