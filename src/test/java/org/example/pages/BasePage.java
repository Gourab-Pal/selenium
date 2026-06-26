package org.example.pages;

import io.qameta.allure.Step;
import org.example.config.TestConfig;
import org.example.core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    // BasePage default constructor
    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.getExplicitWaitSeconds()));
    }

    // Open url
    @Step("Opening url: {url}")
    protected void open(String url) {
        driver.get(url);
    }

    // Returns WebElement for a given By locator when it is visible. Uses ExpectedConditions
    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Returns list of WebElement for a given By locator when it is visible. Uses ExpectedConditions
    protected List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    // Returns WebElement for a given By locator when it is clickable. Uses ExpectedConditions
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Wait for element to be clickable and then click
    @Step("Clicking on element: {locator}")
    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    @Step("Entering text '{text}' into element: {locator}")
    // Wait for element to be visible and enter text
    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    // Extract text from By locator
    protected String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    // returns true if an element defined by locator is visible
    protected boolean isVisible(By locator) {
        return waitForVisible(locator).isDisplayed();
    }

    // returns true if an element defined by locator is clickable/enabled
    protected boolean isEnabled(By locator) {
        return waitForClickable(locator).isEnabled();
    }

}
