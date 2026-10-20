package com.swaglabs.driver;

import com.swaglabs.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

/**
 * Creates, provides and quits the WebDriver for the current thread.
 * Each test thread gets its own browser, so tests can run in parallel.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        DRIVER.set(createDriver(ConfigReader.getBrowser()));
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialised for this thread; call initDriver() first");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                // Clear the thread's driver even if quit() fails, so it is never reused
                DRIVER.remove();
            }
        }
    }

    private static WebDriver createDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> new ChromeDriver(chromeOptions());
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
        }
        // SauceDemo's shared password triggers Chrome's password-manager and
        // "password found in a data breach" popups, which block clicks.
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false
        ));
        return options;
    }
}