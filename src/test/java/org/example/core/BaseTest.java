//package org.example.core;
//
//import io.qameta.allure.Allure;
//import io.qameta.allure.Attachment;
//import io.qameta.allure.Step;
//import io.qameta.allure.testng.AllureTestNg;
//import org.example.config.TestConfig;
//import org.example.db.TestRunContext;
//import org.example.db.TestRunService;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.testng.ITestResult;
//import org.testng.annotations.*;
//import org.example.db.SupabaseDB;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import org.example.db.TestCaseResultService;
//import java.util.UUID;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//@Listeners(AllureTestNg.class)
//public abstract class BaseTest {
//
//    protected static int totalTests = 0;
//    protected static int passedTests = 0;
//    protected static int failedTests = 0;
//    private long suiteStartTime;
//    private long testStartTime;
//
//    protected WebDriver driver;
//
//    // ✅ ONE RUN PER EXECUTION
//    @BeforeSuite
//    public void beforeSuite() {
//
//        suiteStartTime = System.currentTimeMillis();
//        String runId = TestRunService.createTestRun();
//        TestRunContext.setTestRunId(runId);
//
//        System.out.println("Test Run Created: " + runId);
//    }
//
//    @Step("Setup Driver")
//    @BeforeMethod
//    public void setUpDriver() {
//
//        testStartTime = System.currentTimeMillis();
//        DriverManager.initDriver();
//        driver = DriverManager.getDriver();
//
//        Allure.parameter("browser", TestConfig.getBrowser());
//        Allure.parameter("headless", String.valueOf(TestConfig.isHeadless()));
//    }
//
//    @Step("Teardown")
//    @AfterMethod
//    public void tearDownDriver(ITestResult result) {
//        totalTests++;
//
//        if (result.getStatus() == ITestResult.SUCCESS) {
//            passedTests++;
//        } else if (result.getStatus() == ITestResult.FAILURE) {
//            failedTests++;
//        }
//
//        long endTime = System.currentTimeMillis();
//        long duration = endTime - testStartTime;
//
//        String status;
//
//        switch (result.getStatus()) {
//            case org.testng.ITestResult.SUCCESS:
//                status = "PASSED";
//                break;
//            case org.testng.ITestResult.FAILURE:
//                status = "FAILED";
//                break;
//            case org.testng.ITestResult.SKIP:
//                status = "SKIPPED";
//                break;
//            default:
//                status = "PARTIAL";
//        }
//
//        String errorMessage = null;
//        String stackTrace = null;
//
//        if (result.getThrowable() != null) {
//            errorMessage = result.getThrowable().getMessage();
//
//            StringWriter sw = new StringWriter();
//            result.getThrowable().printStackTrace(new PrintWriter(sw));
//            stackTrace = sw.toString();
//        }
//
//        TestCaseResultService.insertTestCaseResult(
//                UUID.fromString(TestRunContext.getTestRunId()),
//                result.getTestClass().getName(),
//                result.getMethod().getMethodName(),
//                status,
//                duration,
//                errorMessage,
//                stackTrace,
//                TestConfig.getBrowser(),
//                "local",
//                testStartTime,
//                endTime
//        );
//
//        if (driver != null) {
//
//            if (result.getStatus() == ITestResult.FAILURE) {
//                attachFailureArtifacts();
//            }
//
//            DriverManager.quitDriver();
//        }
//    }
//
//    private void attachFailureArtifacts() {
//        try {
//            attachScreenshot(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
//            attachPageSource(driver.getPageSource());
//        } catch (Exception ignored) {}
//    }
//
//    @Attachment(value = "Screenshot", type = "image/png")
//    protected byte[] attachScreenshot(byte[] screenshot) {
//        return screenshot;
//    }
//
//    @Attachment(value = "Page Source", type = "text/html")
//    protected String attachPageSource(String pageSource) {
//        return pageSource;
//    }
//
//    @AfterSuite
//    public void updateTestRunSummary() {
//        long suiteEndTime = System.currentTimeMillis();
//        long duration = suiteEndTime - suiteStartTime;
//
//        String sql = """
//        UPDATE public.test_run
//        SET total_tests = ?,
//            passed_tests = ?,
//            failed_tests = ?,
//            status = ?,
//            duration_ms = ?
//        WHERE id = ?
//    """;
//
//        String finalStatus;
//
//        if (failedTests > 0) {
//            finalStatus = "FAILED";
//        } else if (passedTests > 0) {
//            finalStatus = "PASSED";
//        } else {
//            finalStatus = "PARTIAL";
//        }
//
//        System.out.println("DEBUG testRunId = " + TestRunContext.getTestRunId());
//
//        try (Connection conn = SupabaseDB.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, totalTests);
//            stmt.setInt(2, passedTests);
//            stmt.setInt(3, failedTests);
//            stmt.setString(4, finalStatus);
//            stmt.setLong(5, duration);
//            stmt.setObject(6, java.util.UUID.fromString(TestRunContext.getTestRunId()));
//
//            stmt.executeUpdate();
//
//            System.out.println("Test Run Updated Successfully");
//
//        } catch (Exception e) {
//            //throw new RuntimeException("Failed to update test run summary", e);
//            System.out.println(e.getMessage());
//        }
//    }
//}

package org.example.core;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.example.config.TestConfig;
import org.example.db.TestRunContext;
import org.example.db.TestRunService;
import org.example.db.TestCaseResultService;
import org.example.db.SupabaseDB;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.UUID;
import java.io.PrintWriter;
import java.io.StringWriter;

@Listeners(AllureTestNg.class)
public abstract class BaseTest {

    protected WebDriver driver;

    private long suiteStartTime;
    private long testStartTime;

    // =========================
    // BEFORE SUITE
    // =========================
    @BeforeSuite
    public void beforeSuite() {

        suiteStartTime = System.currentTimeMillis();

        String runId = TestRunService.createTestRun();
        TestRunContext.setTestRunId(runId);

        System.out.println("Test Run Created: " + runId);
    }

    // =========================
    // BEFORE METHOD
    // =========================
    @Step("Setup Driver")
    @BeforeMethod
    public void setUpDriver() {

        testStartTime = System.currentTimeMillis();

        DriverManager.initDriver();
        driver = DriverManager.getDriver();

        io.qameta.allure.Allure.parameter("browser", TestConfig.getBrowser());
        io.qameta.allure.Allure.parameter("headless", String.valueOf(TestConfig.isHeadless()));
    }

    // =========================
    // AFTER METHOD
    // =========================
    @Step("Teardown & Log Result")
    @AfterMethod
    public void tearDownDriver(ITestResult result) {

        long endTime = System.currentTimeMillis();
        long duration = endTime - testStartTime;

        String status;
        Throwable throwable = result.getThrowable();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = "PASSED";
                break;
            case ITestResult.FAILURE:
                status = "FAILED";
                break;
            case ITestResult.SKIP:
                status = "SKIPPED";
                break;
            default:
                status = "PARTIAL";
        }

        String errorMessage = null;
        String stackTrace = null;

        if (throwable != null) {
            errorMessage = throwable.getMessage();

            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            stackTrace = sw.toString();
        }

        // =========================
        // INSERT TEST CASE RESULT
        // =========================
        TestCaseResultService.insertTestCaseResult(
                UUID.fromString(TestRunContext.getTestRunId()),
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                status,
                duration,
                errorMessage,
                stackTrace,
                TestConfig.getBrowser(),
                "local",
                testStartTime,
                endTime
        );

        // =========================
        // FAILURE ARTIFACTS
        // =========================
        if (driver != null && result.getStatus() == ITestResult.FAILURE) {
            attachFailureArtifacts();
        }

        if (driver != null) {
            DriverManager.quitDriver();
        }
    }

    // =========================
    // FAILURE ARTIFACTS
    // =========================
    private void attachFailureArtifacts() {
        try {
            attachScreenshot(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            attachPageSource(driver.getPageSource());
        } catch (Exception ignored) {}
    }

    @Attachment(value = "Screenshot", type = "image/png")
    protected byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }

    @Attachment(value = "Page Source", type = "text/html")
    protected String attachPageSource(String pageSource) {
        return pageSource;
    }

    // =========================
    // AFTER SUITE
    // =========================
    @AfterSuite
    public void updateTestRunSummary() {

        long suiteEndTime = System.currentTimeMillis();
        long duration = suiteEndTime - suiteStartTime;

        String runId = TestRunContext.getTestRunId();

        Map<String, Integer> summary = TestRunService.getTestRunSummary(runId);

        int total = summary.get("total");
        int passed = summary.get("passed");
        int failed = summary.get("failed");

        String finalStatus = (failed > 0) ? "FAILED" : "PASSED";

        String sql = """
        UPDATE public.test_run
        SET total_tests = ?,
            passed_tests = ?,
            failed_tests = ?,
            duration_ms = ?,
            status = ?
        WHERE id = ?
    """;

        try (Connection conn = SupabaseDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, total);
            stmt.setInt(2, passed);
            stmt.setInt(3, failed);
            stmt.setLong(4, duration);
            stmt.setString(5, finalStatus);
            stmt.setObject(6, UUID.fromString(runId));

            stmt.executeUpdate();

            System.out.println("Test Run Updated Successfully");

        } catch (Exception e) {
            System.out.println("Failed to update test run summary: " + e.getMessage());
        }
    }

    // =========================
    // SAFE STATUS CHECK (from DB in future, placeholder now)
    // =========================
    private boolean hasFailedTests() {
        // simple safe logic for now:
        // future improvement: query test_case_result table
        return false;
    }
}