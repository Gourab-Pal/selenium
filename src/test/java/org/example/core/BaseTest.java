package org.example.core;

import io.qameta.allure.Step;
import org.example.db.TestRunContext;
import org.example.db.TestRunService;
import org.example.listeners.RetryAnalyzer;
import org.example.utils.AllureLogger;
import org.example.utils.BaseTestUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.util.ArrayList;

public abstract class BaseTest {
    protected WebDriver driver;
    private long suiteStartTime;
    private long testStartTime;

    @Step("Create test run job")
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

    @Step("Teardown & Log test case result")
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

    @Step("Update test run details in db")
    @AfterSuite
    public void updateTestRunSummary() {
        long suiteEndTime = System.currentTimeMillis();
        long duration = suiteEndTime - suiteStartTime;
        BaseTestUtils.updateTestRunDatabase(duration);
    }
}
