package org.example.pages.saucedemo;

import io.qameta.allure.Step;
import org.example.pages.BasePage;
import org.example.utils.AllureLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class InventoryPage extends BasePage {

    private static final String PRODUCT_CARD_BASE_XPATH = "//div[@data-test='inventory-item-name']/ancestor::div[@data-test='inventory-item']";

    private static final By INVENTORY_CONTAINER = By.id("inventory_container");
    private static final By HAMBURGER_MENU_BTN = By.id("react-burger-menu-btn");
    private static final By SIDENAV_ALL_ITEMS_LINK = By.id("inventory_sidebar_link");
    private static final By SIDENAV_ABOUT_LINK = By.id("about_sidebar_link");
    private static final By SIDENAV_LOGOUT_LINK = By.id("logout_sidebar_link");
    private static final By SIDENAV_RESET_APP_STATE_LINK = By.id("reset_sidebar_link");
    private static final By SIDENAV_CONTAINER_CLOSED_STATE = By.xpath("//div[@class='bm-menu-wrap' and @hidden='true']");
    private static final By SIDENAV_CONTAINER_EXPANDED_STATE = By.xpath("//div[@class='bm-menu-wrap' and not(@hidden='true')]");
    private static final By PRODUCT_CARD = By.xpath("//div[@data-test='inventory-item-name']/ancestor::div[@data-test='inventory-item']");

    @Step("Verify inventory page is displayed")
    public void isInventoryPageLoaded() {
        Assert.assertTrue(waitForVisible(INVENTORY_CONTAINER).isDisplayed(), "Inventory page not loaded correctly");
    }

    @Step("Verify sidenav is in closed state")
    public void verifySidenavInClosedState() {
        try {
            waitForVisible(SIDENAV_CONTAINER_CLOSED_STATE);
        }
        catch (Exception ignored) {
            AllureLogger.log("Sidenav is already in opened state");
        }
    }

    @Step("Verify sidenav is in expanded state")
    public void verifySidenavInExpandedState() {
        try {
            waitForVisible(SIDENAV_CONTAINER_EXPANDED_STATE);
        }
        catch (Exception ignored) {
            AllureLogger.log("Sidenav is already in expanded state");
        }
    }

    @Step("Open sidenav by clicking on hamburger icon")
    public void openSidenav() {
        verifySidenavInClosedState();
        click(HAMBURGER_MENU_BTN);
        verifySidenavInExpandedState();
    }

    @Step("Verify all expected sidenav links are loaded in DOM")
    public void validateAllOptionsInSidenav() {
        openSidenav();
        By[] linkLocators = {SIDENAV_ALL_ITEMS_LINK, SIDENAV_ABOUT_LINK, SIDENAV_LOGOUT_LINK, SIDENAV_RESET_APP_STATE_LINK};
        int expectedLinkCount = linkLocators.length;
        int actualLinkCount = 0;
        for(By linkLocator : linkLocators) {
            try {
                waitForVisible(linkLocator);
                actualLinkCount++;
            } catch (NoSuchElementException e) {
                AllureLogger.log("Element not visible in DOM: " + e.getMessage());
            }
        }
        Assert.assertEquals(actualLinkCount, expectedLinkCount, "All expected links are not loaded in the DOM");
    }

    @Step("Fetching product title")
    public String getProductTitle(int index) {
        String xpath = "(" + PRODUCT_CARD_BASE_XPATH + "//div[@class='inventory_item_label']/a/div)[" + index + "]";
        By locator = By.xpath(xpath);
        return getText(locator);
    }

    @Step("Fetching product description")
    public String getProductDescription(int index) {
        String xpath = "(" + PRODUCT_CARD_BASE_XPATH + "//div[@data-test='inventory-item-desc'])[" + index + "]";
        By locator = By.xpath(xpath);
        return getText(locator);
    }

    @Step("Fetching product price is USD unit")
    public double getProductPrice(int index) {
        String xpath = "(" + PRODUCT_CARD_BASE_XPATH + "//div[@data-test='inventory-item-price'])[" + index + "]";
        By locator = By.xpath(xpath);
        return Double.parseDouble(getText(locator).trim().replace("$", "").trim());
    }

    @Step("Fetching add to cart button state")
    public boolean isAddToCartButtonClickable(int index) {
        String xpath = "(" + PRODUCT_CARD_BASE_XPATH + "//button)[" + index + "]";
        By locator = By.xpath(xpath);
        return waitForClickable(locator).isEnabled();
    }

    @Step("Validate product title, description, price and add to cart button state")
    public void validateProductCardDetails() {
        List<WebElement> productCards = waitForAllVisible(PRODUCT_CARD);
        for(int i=1; i<=productCards.size(); i++) {
            SoftAssert softAssert = new SoftAssert();

            String productTitle = getProductTitle(i);
            softAssert.assertNotNull(productTitle, "product title is found null");
            softAssert.assertFalse(productTitle.isEmpty(), "product title is blank");

            String productDescription = getProductDescription(i);
            softAssert.assertNotNull(productDescription, "product description is found null");
            softAssert.assertFalse(productDescription.isEmpty(), "product description is blank");

            double productPrice = getProductPrice(i);
            softAssert.assertTrue(productPrice!=0, "product price can not be zero");

            boolean addToCardState = isAddToCartButtonClickable(i);
            softAssert.assertTrue(addToCardState, "add to cart button is found disabled");

            softAssert.assertAll();
        }
    }


}
