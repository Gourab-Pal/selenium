package org.example.tests.SauceDemo;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.core.BaseTest;
import org.example.pages.saucedemo.InventoryPage;
import org.example.pages.saucedemo.LoginPage;
import org.testng.annotations.Test;

@Epic("Swag Lab Site")
@Feature("Sauce Demo Login")
public class LoginTest extends BaseTest {

    @Test(description = "Test existence of basic elements in login page", groups = {"regression"})
    @Story("Basic element visible")
    @Severity(SeverityLevel.NORMAL)
    public void loginChecks() {
        LoginPage loginPage = new LoginPage();
        loginPage.performCriticalChecks();
        loginPage.performBasicChecks();
    }

    @Test(description = "Should log in with standard user and see inventory page", groups = {"smoke", "sanity"}, dependsOnMethods = "loginChecks")
    @Story("Standard user can log in")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldLoginWithStandardUser() {
        InventoryPage inventoryPage = new LoginPage()
                .loginAs(System.getenv("SAUCEDEMO_USERNAME"), System.getenv("SAUCEDEMO_PASSWORD"));
        inventoryPage.isInventoryPageLoaded();
    }
}
