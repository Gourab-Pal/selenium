package org.example.tests.SauceDemo;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.core.BaseTest;
import org.example.pages.saucedemo.InventoryPage;
import org.example.pages.saucedemo.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

@Epic("Swag Lab Site")
@Feature("Sauce Demo Login")
public class LoginTest extends BaseTest {

    @Test(description = "Should log in with standard user and see inventory page", groups = {"smoke", "sanity"})
    @Story("Standard user can log in")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldLoginWithStandardUser() {
        InventoryPage inventoryPage = new LoginPage().loginAs("standard_user", "secret_sauce");
        inventoryPage.isInventoryPageLoaded();
        throw new NoSuchElementException("test element not found in DOM");
    }
}
