package org.example.pages.theinternet;

import io.qameta.allure.Step;
import org.example.config.TestConfig;
import org.example.pages.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");

    @Step("Open The Internet login page")
    public LoginPage open() {
        open(TestConfig.getTheInternetBaseUrl() + "/login");
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
    public SecureAreaPage submitLogin() {
        click(LOGIN_BUTTON);
        return new SecureAreaPage();
    }

    public SecureAreaPage loginAs(String username, String password) {
        return open()
                .enterUsername(username)
                .enterPassword(password)
                .submitLogin();
    }
}
