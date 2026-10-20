package com.swaglabs.pages;

import com.swaglabs.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Parent of all page objects. Every interaction waits for its element first,
 * so pages never need Thread.sleep or raw findElement calls.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private final Duration slowMotion;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, ConfigReader.getTimeout());
        this.slowMotion = ConfigReader.getSlowMotion();
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        slowDown();
        element.click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        slowDown();
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        WebElement element = waitForVisible(locator);
        slowDown();
        return element.getText();
    }

    /** Waits up to the configured timeout for the element to become visible. */
    protected boolean waitUntilVisible(By locator) {
        try {
            waitForVisible(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Texts of all matching elements, without waiting; call once the page is loaded. Empty if none match. */
    protected List<String> getTexts(By locator) {
        return driver.findElements(locator).stream().map(WebElement::getText).toList();
    }

    /** Checks the current state without waiting, so it can also confirm an element is absent. */
    protected boolean isDisplayed(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    /** Pauses for the configured slow-motion delay so a run can be followed by eye; no-op when it is 0. */
    private void slowDown() {
        if (slowMotion.isZero()) {
            return;
        }
        try {
            Thread.sleep(slowMotion);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}