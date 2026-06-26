package org.example.pages.saucedemo;

import io.qameta.allure.Step;
import org.example.config.TestConfig;
import org.example.pages.BasePage;
import org.example.utils.AllureLogger;
import org.openqa.selenium.By;
import org.testng.Assert;

public class LoginPage extends BasePage {
    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By LOGIN_LOGO = By.className(".login_logo");
    private static final By LOGIN_CREDENTIAL_WRAPPER = By.id("login_credentials");
    private static final By LOGIN_PASSWORD_WRAPPER = By.id("login_password");

    @Step("Open Sauce Demo login page")
    public LoginPage open() {
        open(TestConfig.getSauceDemoBaseUrl() + "/");
        return this;
    }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        type(USERNAME, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        type(PASSWORD, password);
        return this;
    }

    @Step("Submit login form")
    public InventoryPage submitLogin() {
        click(LOGIN_BUTTON);
        return new InventoryPage();
    }

    public InventoryPage loginAs(String username, String password) {
        return open()
                .enterUsername(username)
                .enterPassword(password)
                .submitLogin();
    }

    public void performBasicChecks() {
        try {
            waitForVisible(LOGIN_LOGO);
            waitForVisible(USERNAME);
            waitForVisible(PASSWORD);
            waitForVisible(LOGIN_BUTTON);
            waitForVisible(LOGIN_CREDENTIAL_WRAPPER);
            waitForVisible(LOGIN_PASSWORD_WRAPPER);
        } catch (Exception e) {
            AllureLogger.log("Basic exception occurred: " + e);
        }
    }

    public void performCriticalChecks() {
        try {
            open();
            waitForVisible(USERNAME);
            waitForVisible(PASSWORD);
            waitForVisible(LOGIN_BUTTON);
        } catch (Exception e) {
            Assert.fail("Critical exception occurred: " + e);
        }
    }
}
