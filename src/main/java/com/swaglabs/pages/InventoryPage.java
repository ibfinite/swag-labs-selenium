package com.swaglabs.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {

    private final By inventoryContainer = By.cssSelector("[data-test='inventory-container']");
    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUntilVisible(inventoryContainer);
    }

    @Step("Read page title")
    public String getTitle() {
        return getText(pageTitle);
    }

    /**
     * Adds a product using its id, e.g. "sauce-labs-backpack" clicks #add-to-cart-sauce-labs-backpack.
     */
    @Step("Add product \"{productId}\" to cart")
    public InventoryPage addToCart(String productId) {
        click(By.id("add-to-cart-" + productId));
        return this;
    }

    /** Number shown on the cart badge; 0 when the cart is empty and the badge is not shown. */
    @Step("Read cart badge count")
    public int getCartBadgeCount() {
        return isDisplayed(cartBadge) ? Integer.parseInt(getText(cartBadge)) : 0;
    }

    @Step("Open cart")
    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }
}