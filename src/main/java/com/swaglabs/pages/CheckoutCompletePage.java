package com.swaglabs.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Order confirmation shown after finishing checkout. */
public class CheckoutCompletePage extends BasePage {

    private final By completeContainer = By.cssSelector("[data-test='checkout-complete-container']");
    private final By completeHeader = By.cssSelector("[data-test='complete-header']");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.cssSelector("[data-test='logout-sidebar-link']");

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

    @Step("Log out")
    public LoginPage logout() {
        openMenu();
        clickLogout();
        return new LoginPage(driver);
    }

    @Step("Open hamburger menu")
    private void openMenu() {
        click(menuButton);
    }

    @Step("Click Logout")
    private void clickLogout() {
        click(logoutLink);
    }
}