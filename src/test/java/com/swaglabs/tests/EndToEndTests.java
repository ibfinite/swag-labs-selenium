package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.CheckoutCompletePage;
import com.swaglabs.pages.CheckoutInformationPage;
import com.swaglabs.pages.CheckoutOverviewPage;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.LoginPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.swaglabs.data.TestData.*;

@Feature("Checkout")
public class EndToEndTests extends BaseTest {

    @Test(description = "Standard user logs in, adds a product to the cart and completes checkout")
    @Severity(SeverityLevel.BLOCKER)
    public void purchaseSingleProduct() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoaded(), "Login page did not load");

        InventoryPage inventoryPage = loginPage.loginAs(STANDARD_USERNAME, PASSWORD);
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load after login");
        Assert.assertEquals(inventoryPage.getTitle(), PRODUCTS_TITLE, "Page title");
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 0, "Cart badge count before adding a product");

        inventoryPage.addToCart(BACKPACK_ID);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Cart badge count after adding a product");

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page did not load");
        Assert.assertEquals(cartPage.getItemNames(), List.of(BACKPACK_NAME), "Items in cart");

        CheckoutInformationPage informationPage = cartPage.checkout();
        Assert.assertTrue(informationPage.isLoaded(), "Checkout information page did not load");

        CheckoutOverviewPage overviewPage = informationPage
                .fillInformation(FIRST_NAME, LAST_NAME, POSTAL_CODE)
                .continueToOverview();
        Assert.assertTrue(overviewPage.isLoaded(), "Checkout overview page did not load");
        Assert.assertEquals(overviewPage.getItemNames(), List.of(BACKPACK_NAME), "Items in order overview");

        CheckoutCompletePage completePage = overviewPage.finish();
        Assert.assertTrue(completePage.isLoaded(), "Checkout complete page did not load");
        Assert.assertEquals(completePage.getConfirmationHeader(), ORDER_CONFIRMATION_HEADER, "Confirmation header");
    }
}