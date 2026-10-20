package com.swaglabs.data;

import com.swaglabs.config.ConfigReader;

/**
 * Test data shared by the tests. Credentials are read from configuration, never hard-coded here.
 */
public final class TestData {

    public static final String STANDARD_USERNAME = ConfigReader.getUsername("standard");
    public static final String PASSWORD = ConfigReader.getPassword();
    public static final String WRONG_PASSWORD = "wrong_password";

    public static final String PRODUCTS_TITLE = "Products";

    public static final String INVALID_CREDENTIALS_ERROR =
            "Epic sadface: Username and password do not match any user in this service";
    public static final String USERNAME_REQUIRED_ERROR = "Epic sadface: Username is required";
    public static final String PASSWORD_REQUIRED_ERROR = "Epic sadface: Password is required";

    public static final String BACKPACK_ID = "sauce-labs-backpack";
    public static final String BACKPACK_NAME = "Sauce Labs Backpack";

    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String POSTAL_CODE = "12345";

    public static final String ORDER_CONFIRMATION_HEADER = "Thank you for your order!";

    private TestData() {
    }
}