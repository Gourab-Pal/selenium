package org.example.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class BrowserFactory {

    private BrowserFactory() {
    }

    public static WebDriver createDriver() {
        WebDriver driver = switch (TestConfig.getBrowser().toLowerCase()) {
            case "firefox" -> new FirefoxDriver(buildFirefoxOptions());
            case "edge" -> new EdgeDriver(buildEdgeOptions());
            default -> new ChromeDriver(buildChromeOptions());
        };

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.getImplicitWaitSeconds()));
        driver.manage().window().maximize();
        return driver;
    }

    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        if (TestConfig.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--window-size=1920,1080"
        );
        return options;
    }

    private static FirefoxOptions buildFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (TestConfig.isHeadless()) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions buildEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        if (TestConfig.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--window-size=1920,1080"
        );
        return options;
    }
}
