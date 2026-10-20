package com.swaglabs.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Allure records every argument of a @Step method in the results files, so the methods that receive
 * the password report their steps with Allure.step(...) instead, keeping the password out of the results.
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.cssSelector("[data-test='username']");
    private final By passwordInput = By.cssSelector("[data-test='password']");
    private final By loginButton = By.cssSelector("[data-test='login-button']");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUntilVisible(loginButton);
    }

    /** Logs in with credentials that are expected to succeed. */
    public InventoryPage loginAs(String username, String password) {
        Allure.step("Log in as \"" + username + "\"", () -> submitCredentials(username, password));
        return new InventoryPage(driver);
    }

    /** Logs in with credentials that are expected to be rejected, staying on the login page. */
    public LoginPage loginExpectingError(String username, String password) {
        Allure.step("Attempt to log in as \"" + username + "\"", () -> submitCredentials(username, password));
        return this;
    }

    @Step("Read login error message")
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    private void submitCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    @Step("Enter username \"{username}\"")
    private void enterUsername(String username) {
        type(usernameInput, username);
    }

    private void enterPassword(String password) {
        Allure.step("Enter password", () -> type(passwordInput, password));
    }

    @Step("Click Login")
    private void clickLogin() {
        click(loginButton);
    }
}