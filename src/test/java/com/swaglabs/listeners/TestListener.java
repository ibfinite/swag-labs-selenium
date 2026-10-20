package com.swaglabs.listeners;

import com.swaglabs.driver.DriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * Attaches a screenshot to the Allure report when a test fails.
 * <p>
 * Uses afterInvocation rather than ITestListener.onTestFailure: TestNG calls it straight after
 * the test method, before @AfterMethod has quit the browser, and while Allure still has the test open.
 */
public class TestListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod() && result.getStatus() == ITestResult.FAILURE) {
            attachScreenshot();
        }
    }

    private void attachScreenshot() {
        try {
            byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.attachment("Screenshot on failure", "image/png", new ByteArrayInputStream(screenshot),
                    AttachmentOptions.withFileExtension("png"));
        } catch (RuntimeException e) {
            // A failed screenshot must not hide the real test failure
            System.err.println("Could not capture screenshot: " + e.getMessage());
        }
    }
}