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

@Epic("Swag Lab Site")
@Feature("Sauce Demo Login")
public class InventoryTest extends BaseTest {

    @Test(description = "Should load all expected link in sidenav container", groups = {"smoke", "sanity"})
    @Story("Sidenav should load")
    @Severity(SeverityLevel.NORMAL)
    public void sidebarLinksShouldVisible() {
        InventoryPage inventoryPage = new LoginPage().loginAs("standard_user", "secret_sauce");
        Assert.fail("slack error checking test");
        inventoryPage.validateAllOptionsInSidenav();
    }
}
