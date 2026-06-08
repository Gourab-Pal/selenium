package org.example.core;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.testng.AllureTestNg;
import org.example.config.TestConfig;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(AllureTestNg.class)
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUpDriver() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        Allure.parameter("browser", TestConfig.getBrowser());
        Allure.parameter("headless", String.valueOf(TestConfig.isHeadless()));
    }

    @AfterMethod
    public void tearDownDriver(ITestResult result) {
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
}
