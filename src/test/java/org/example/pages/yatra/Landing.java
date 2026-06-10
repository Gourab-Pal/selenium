package org.example.pages.yatra;

import org.example.pages.BasePage;
import io.qameta.allure.Step;
import org.example.config.TestConfig;
import org.openqa.selenium.By;
import org.testng.Assert;

public class Landing extends BasePage {

    private static final By LOGIN_SIGNUP_BUTTON = By.xpath("//div[contains(text(), 'Login') or contains(text(), 'Signup')]");
    private static final By LOGIN_SIGNUP_MODAL_LABEL = By.xpath("//p[@for='mobile-number']");
    private static final By MOBILE_NUMBER_INPUT = By.id("mobile-number");
    private static final By LOGIN_BUTTON = By.xpath("//button[contains(text(), 'Login')]");


    @Step("Open Yatra")
    public Landing openYatra() {
        open(TestConfig.getYatraBaseUrl());
        return this;
    }

    @Step("Proceed to email verification")
    public void proceedToOtpVerification() {
        click(LOGIN_SIGNUP_BUTTON);
        Assert.assertTrue(isVisible(LOGIN_SIGNUP_MODAL_LABEL));
        type(MOBILE_NUMBER_INPUT, "testuser@example.com");
        click(LOGIN_BUTTON);
    }

    public void validateOtpFlow() {
        openYatra().proceedToOtpVerification();
    }
}
