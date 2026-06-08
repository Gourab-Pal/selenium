package org.example.pages.saucedemo;

import io.qameta.allure.Step;
import org.example.config.TestConfig;
import org.example.pages.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");

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
}
