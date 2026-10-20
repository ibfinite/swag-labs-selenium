package com.swaglabs.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Order confirmation shown after finishing checkout. */
public class CheckoutCompletePage extends BasePage {

    private final By completeContainer = By.cssSelector("[data-test='checkout-complete-container']");
    private final By completeHeader = By.cssSelector("[data-test='complete-header']");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUntilVisible(completeContainer);
    }

    @Step("Read confirmation header")
    public String getConfirmationHeader() {
        return getText(completeHeader);
    }
}