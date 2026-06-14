package org.example.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtils {
    private ScreenshotUtils() {}

    public static String saveScreenshot(WebDriver driver, String testName) {

        try {

            Path screenshotDir = Paths.get("build", "screenshots");
            Files.createDirectories(screenshotDir);

            String fileName =
                    testName + "_" + System.currentTimeMillis() + ".png";

            Path screenshotPath = screenshotDir.resolve(fileName);

            byte[] screenshot =
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            Files.write(screenshotPath, screenshot);

            return screenshotPath.toString();

        } catch (Exception e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }

}
