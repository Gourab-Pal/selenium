package org.example.pages.saucedemo;

import io.qameta.allure.Step;
import org.example.pages.BasePage;
import org.openqa.selenium.By;

public class InventoryPage extends BasePage {

    private static final By INVENTORY_CONTAINER = By.id("inventory_container");
    private static final By PAGE_TITLE = By.cssSelector(".title");

    @Step("Verify inventory page is displayed")
    public boolean isDisplayed() {
        return waitForVisible(INVENTORY_CONTAINER).isDisplayed();
    }

    @Step("Get inventory page title")
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }
}
