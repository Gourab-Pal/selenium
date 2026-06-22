package org.example.utils;

import io.qameta.allure.Attachment;
import org.example.config.TestConfig;
import org.example.db.SupabaseDB;
import org.example.db.TestCaseResultService;
import org.example.db.TestRunContext;
import org.example.db.TestRunService;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class BaseTestUtils {
    public static String status;
    public static String errorMessage;
    public static String stackTrace;
    public static String screenshotPath;

    public static String getTestCaseStatus(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = TestConfig.getPassedStatus();
                break;
            case ITestResult.FAILURE:
                status = TestConfig.getFailedStatus();
                break;
            case ITestResult.SKIP:
                status = TestConfig.getSkippedStatus();
                break;
            default:
                status = TestConfig.getPartialStatus();
        }
        return status;
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] attachScreenshot(byte[] screenshot) {return screenshot;}

    @Attachment(value = "Page Source", type = "text/html")
    public static String attachPageSource(String pageSource) {return pageSource;}

    public static void attachFailureArtifacts(WebDriver driver) {
        try {
            attachScreenshot(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            attachPageSource(driver.getPageSource());
        }
        catch (Exception ignored) {}
    }

    public static ArrayList<String> getErrorDetails(ITestResult result) {
        ArrayList<String> errorDetails = new ArrayList<>();
        Throwable throwable = result.getThrowable();

        if (throwable != null) {
            errorMessage = throwable.getMessage();
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            stackTrace = sw.toString();
        }
        errorDetails.add(errorMessage);
        errorDetails.add(stackTrace);
        return errorDetails;
    }

    public static String getScreenshotPath(WebDriver driver, ITestResult result) {
        if (driver != null && result.getStatus() == ITestResult.FAILURE) {
            screenshotPath = ScreenshotUtils.saveScreenshot(driver, result.getMethod().getMethodName());
            BaseTestUtils.attachFailureArtifacts(driver);
        }
        return screenshotPath;
    }

    public static void insertTestCaseResultIntoDatabase(ITestResult result, long duration, long testStartTime,
                                                        long endTime, int retryCount, String status, String errorMessage,
                                                        String stackTrace, String screenshotPath) {
        TestCaseResultService.insertTestCaseResult(
                UUID.fromString(TestRunContext.getTestRunId()),
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                status,
                duration,
                errorMessage,
                stackTrace,
                screenshotPath,
                TestConfig.getBrowser(),
                BrowserVersionUtils.getBrowserVersion(),
                System.getenv().getOrDefault("ENVIRONMENT", TestConfig.getLocalEnv()),
                testStartTime,
                endTime,
                retryCount
        );
    }

    public static void updateTestRunDatabase(long duration) {
        String runId = TestRunContext.getTestRunId();
        Map<String, Integer> summary = TestRunService.getTestRunSummary(runId);
        int total = summary.get("total");
        int passed = summary.get("passed");
        int failed = summary.get("failed");
        String finalStatus = (failed > 0) ? TestConfig.getFailedStatus() : TestConfig.getPassedStatus();
        String sql = """
        UPDATE %s
        SET total_tests = ?,
            passed_tests = ?,
            failed_tests = ?,
            duration_ms = ?,
            status = ?
        WHERE id = ?
        """.formatted(TestConfig.getTestRunTableName());

        try (Connection conn = SupabaseDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, total);
            stmt.setInt(2, passed);
            stmt.setInt(3, failed);
            stmt.setLong(4, duration);
            stmt.setString(5, finalStatus);
            stmt.setObject(6, UUID.fromString(runId));

            stmt.executeUpdate();
            AllureLogger.log("Test Run Updated Successfully");

        } catch (Exception e) {
            throw new RuntimeException("Failed to update test run summary: " + e.getMessage());
        }
    }

}
