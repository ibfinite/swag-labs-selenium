package com.swaglabs.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** Checkout step two: order summary before finishing. */
public class CheckoutOverviewPage extends BasePage {

    private final By summaryContainer = By.cssSelector("[data-test='checkout-summary-container']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By finishButton = By.cssSelector("[data-test='finish']");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUntilVisible(summaryContainer);
    }

    @Step("Read order item names")
    public List<String> getItemNames() {
        waitForVisible(summaryContainer);
        return getTexts(itemName);
    }

    @Step("Click Finish")
    public CheckoutCompletePage finish() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }
}