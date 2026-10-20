package com.swaglabs.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step one: the buyer's name and postal code. */
public class CheckoutInformationPage extends BasePage {

    private final By infoContainer = By.cssSelector("[data-test='checkout-info-container']");
    private final By firstNameInput = By.cssSelector("[data-test='firstName']");
    private final By lastNameInput = By.cssSelector("[data-test='lastName']");
    private final By postalCodeInput = By.cssSelector("[data-test='postalCode']");
    private final By continueButton = By.cssSelector("[data-test='continue']");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUntilVisible(infoContainer);
    }

    @Step("Fill checkout information: \"{firstName}\" \"{lastName}\", \"{postalCode}\"")
    public CheckoutInformationPage fillInformation(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        return this;
    }

    /** Continues with information that is expected to be accepted. */
    @Step("Continue to overview")
    public CheckoutOverviewPage continueToOverview() {
        click(continueButton);
        return new CheckoutOverviewPage(driver);
    }

    /** Continues with information that is expected to be rejected, staying on this page. */
    @Step("Continue expecting a validation error")
    public CheckoutInformationPage continueExpectingError() {
        click(continueButton);
        return this;
    }

    @Step("Read checkout error message")
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}