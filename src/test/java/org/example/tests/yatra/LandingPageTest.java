package org.example.tests.yatra;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.core.BaseTest;
import org.example.pages.yatra.Landing;
import org.testng.annotations.Test;


@Epic("Travel Site")
@Feature("The Yatra Login")
public class LandingPageTest extends BaseTest {

    @Test(description = "Validates user landing on landing page and otp flow")
    @Story("Invalid number and otp throws meaningfully error message")
    @Severity(SeverityLevel.CRITICAL)
    public void yt_001() {
        Landing landing = new Landing();
        landing.validateOtpFlow();
    }
}
