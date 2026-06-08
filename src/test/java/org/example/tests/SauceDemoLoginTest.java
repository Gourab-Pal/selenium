package org.example.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.core.BaseTest;
import org.example.pages.saucedemo.InventoryPage;
import org.example.pages.saucedemo.LoginPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Demo Sites")
@Feature("Sauce Demo Login")
public class SauceDemoLoginTest extends BaseTest {

    @Test(description = "Should log in with standard user and see inventory page")
    @Story("Standard user can log in")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldLoginWithStandardUser() {
        InventoryPage inventoryPage = new LoginPage().loginAs("standard_user", "secret_sauce");

        assertThat(inventoryPage.isDisplayed()).isTrue();
        assertThat(inventoryPage.getPageTitle()).isEqualTo("Products");
    }
}
