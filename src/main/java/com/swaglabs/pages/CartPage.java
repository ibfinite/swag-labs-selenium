package com.swaglabs.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartList = By.cssSelector("[data-test='cart-list']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By checkoutButton = By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUntilVisible(cartList);
    }

    @Step("Read cart item names")
    public List<String> getItemNames() {
        waitForVisible(cartList);
        return getTexts(itemName);
    }

    @Step("Click Checkout")
    public CheckoutInformationPage checkout() {
        click(checkoutButton);
        return new CheckoutInformationPage(driver);
    }
}