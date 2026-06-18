package org.example.utils;

import org.example.core.DriverManager;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;

public class BrowserVersionUtils {

    public static String getBrowserVersion() {
        try {
            WebDriver driver = DriverManager.getDriver();

            return ((HasCapabilities) driver)
                    .getCapabilities()
                    .getBrowserVersion();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
}