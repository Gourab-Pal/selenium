package org.example.core;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.example.config.TestConfig;
import org.example.db.TestResultRepository;
import org.example.config.TestConfig;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.util.TimeZone;

@Listeners(AllureTestNg.class)
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeSuite
    public void globalSetup() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }

    @Step("Attaching web driver")
    @BeforeMethod
    public void setUpDriver() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        Allure.parameter("browser", TestConfig.getBrowser());
        Allure.parameter("headless", String.valueOf(TestConfig.isHeadless()));
    }

    @Step("Attaching failure artifacts and removing web driver")
    @AfterMethod
    public void tearDownDriver(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        String status = getStatus(result);
        String browser = TestConfig.getBrowser();

        // ✅ SAVE TO POSTGRES
        TestResultRepository.saveResult(testName, status, browser);

        if (driver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                attachFailureArtifacts();
            }
            DriverManager.quitDriver();
        }
    }

    private void attachFailureArtifacts() {
        try {
            attachScreenshot(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            attachPageSource(driver.getPageSource());
        } catch (Exception ignored) {
            // Driver may already be closed or page unavailable.
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    protected byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }

    @Attachment(value = "Page Source", type = "text/html")
    protected String attachPageSource(String pageSource) {
        return pageSource;
    }

    private String getStatus(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                return "PASS";
            case ITestResult.FAILURE:
                return "FAIL";
            case ITestResult.SKIP:
                return "SKIP";
            default:
                return "UNKNOWN";
        }
    }
}
