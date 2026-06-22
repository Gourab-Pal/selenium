package org.example.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class BaseTestUtils {
    public static String status;
    public static String errorMessage;
    public static String stackTrace;

    public static String getTestCaseStatus(ITestResult result) {
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

}
