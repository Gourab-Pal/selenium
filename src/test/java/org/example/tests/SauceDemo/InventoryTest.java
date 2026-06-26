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
@Feature("Inventory page tests")
public class InventoryTest extends BaseTest {

    @Test(description = "Should load all expected link in sidenav container", groups = {"smoke", "sanity"})
    @Story("Sidenav should load")
    @Severity(SeverityLevel.NORMAL)
    public void sidebarLinksShouldVisible() {
        InventoryPage inventoryPage = new LoginPage().loginAs(System.getenv("SAUCEDEMO_USERNAME"), System.getenv("SAUCEDEMO_PASSWORD"));
        inventoryPage.validateAllOptionsInSidenav();
    }

    @Test(description = "Checking all product card details in inventory page", groups = {"smoke", "sanity"})
    @Story("Product card details validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testProducts() {
        InventoryPage inventoryPage = new LoginPage().loginAs(System.getenv("SAUCEDEMO_USERNAME"), System.getenv("SAUCEDEMO_PASSWORD"));
        inventoryPage.validateProductCardDetails();
    }
}
