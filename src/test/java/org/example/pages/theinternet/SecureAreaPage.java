package org.example.pages.theinternet;

import io.qameta.allure.Step;
import org.example.pages.BasePage;
import org.openqa.selenium.By;

public class SecureAreaPage extends BasePage {

    private static final By HEADING = By.cssSelector("#content h2");
    private static final By SUCCESS_BANNER = By.cssSelector(".flash.success");

    @Step("Get secure area heading text")
    public String getHeadingText() {
        return getText(HEADING);
    }

    @Step("Get success banner text")
    public String getSuccessBannerText() {
        return getText(SUCCESS_BANNER);
    }
}
