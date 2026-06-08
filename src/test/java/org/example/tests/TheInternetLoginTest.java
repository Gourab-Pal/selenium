package org.example.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.core.BaseTest;
import org.example.pages.theinternet.LoginPage;
import org.example.pages.theinternet.SecureAreaPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Demo Sites")
@Feature("The Internet Login")
public class TheInternetLoginTest extends BaseTest {

    @Test(description = "Should log in with valid credentials and reach secure area")
    @Story("Valid user can log in")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldLoginWithValidCredentials() {
        SecureAreaPage secureAreaPage = new LoginPage().loginAs("tomsmith", "SuperSecretPassword!");

        assertThat(secureAreaPage.getHeadingText()).isEqualTo("Secure Area");
        assertThat(secureAreaPage.getSuccessBannerText()).contains("You logged into a secure area!");
    }
}
