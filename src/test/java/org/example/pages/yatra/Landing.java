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
    private static final By OTP_INPUT = By.id("otp");
    private static final By VERIFY_BUTTON = By.xpath("//button[contains(text(), 'Verify')]");
    private static final By INCORRECT_OTP_TEXT = By.xpath("//p[contains(text(), 'incorrect') and contains(text(), 'expired')]");


    @Step("Open Yatra")
    public Landing openYatra() {
        open(TestConfig.getYatraBaseUrl());
        return this;
    }

    @Step("Proceed to otp verification")
    public void proceedToOtpVerification() {
        click(LOGIN_SIGNUP_BUTTON);
        Assert.assertTrue(isVisible(LOGIN_SIGNUP_MODAL_LABEL));
        type(MOBILE_NUMBER_INPUT, "1122");
        type(MOBILE_NUMBER_INPUT, "1122334455");
        click(LOGIN_BUTTON);
        type(OTP_INPUT, "000000");
        click(VERIFY_BUTTON);
        Assert.assertTrue(isVisible(INCORRECT_OTP_TEXT));
    }

    public void validateOtpFlow() {
        openYatra().proceedToOtpVerification();
    }
}
