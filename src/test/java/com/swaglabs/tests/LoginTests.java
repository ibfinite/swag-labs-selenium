package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.LoginPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.swaglabs.data.TestData.*;

@Feature("Login")
public class LoginTests extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void openLoginPage() {
        loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoaded(), "Login page did not load");
    }

    @Test(description = "Standard user can log in and lands on the Products page")
    @Severity(SeverityLevel.BLOCKER)
    public void validLogin() {
        InventoryPage inventoryPage = loginPage.loginAs(STANDARD_USERNAME, PASSWORD);

        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load after login");
        Assert.assertEquals(inventoryPage.getTitle(), PRODUCTS_TITLE, "Page title");
    }

    @Test(description = "Login fails with a wrong password")
    @Severity(SeverityLevel.CRITICAL)
    public void wrongPasswordLogin() {
        loginPage.loginExpectingError(STANDARD_USERNAME, WRONG_PASSWORD);

        Assert.assertEquals(loginPage.getErrorMessage(), INVALID_CREDENTIALS_ERROR, "Error message");
    }

    @Test(description = "Login fails when the username is empty")
    @Severity(SeverityLevel.NORMAL)
    public void emptyUsernameLogin() {
        loginPage.loginExpectingError("", PASSWORD);

        Assert.assertEquals(loginPage.getErrorMessage(), USERNAME_REQUIRED_ERROR, "Error message");
    }

    @Test(description = "Login fails when the password is empty")
    @Severity(SeverityLevel.NORMAL)
    public void emptyPasswordLogin() {
        loginPage.loginExpectingError(STANDARD_USERNAME, "");

        Assert.assertEquals(loginPage.getErrorMessage(), PASSWORD_REQUIRED_ERROR, "Error message");
    }
}