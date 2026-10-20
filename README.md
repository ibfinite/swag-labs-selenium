# Swag Labs UI Automation

UI test automation framework for [saucedemo.com](https://www.saucedemo.com), built with the Page Object Model.

## Tech stack

| Tool | Version |
|---|---|
| Java (Amazon Corretto) | 26 |
| Selenium WebDriver | 4.51.0 |
| TestNG | 7.12.0 |
| Allure (allure-testng) | 3.0.0 |
| Gradle (wrapper included) | 9.6 |

## Prerequisites

- **JDK 26.** Gradle builds with a Java 26 toolchain, so JDK 26 must be installed.
- **Google Chrome.** Selenium Manager downloads the matching ChromeDriver automatically.
- Internet access for Gradle dependencies and the site under test.

## Running the tests

```bash
./gradlew test                     # macOS / Linux
gradlew.bat test                   # Windows
```

Run a single class:

```bash
./gradlew test --tests "com.swaglabs.tests.EndToEndTests"
```

## Test cases

| Class | Test | Verifies |
|---|---|---|
| `LoginTests` | `validLogin` | Standard user logs in and lands on the Products page |
| | `wrongPasswordLogin` | Wrong password shows the "do not match" error |
| | `emptyUsernameLogin` | Empty username shows "Username is required" |
| | `emptyPasswordLogin` | Empty password shows "Password is required" |
| `EndToEndTests` | `purchaseSingleProduct` | Login → add Sauce Labs Backpack → cart → checkout information → overview → finish → logout, asserting each step |

## Allure report

```bash
./gradlew allureServe              # generate the report from the last run and open it in the browser
./gradlew allureReport             # generate only, into build/reports/allure-report/
```

The first report run downloads Node.js and the Allure report tool into `build/`, so it takes a few minutes.
Each page action appears as a step in the report, and failed tests get a screenshot attached.

## Configuration

Settings live in `src/main/resources/config.properties`:

| Key | Default | Description |
|---|---|---|
| `base.url` | `https://www.saucedemo.com` | Site under test |
| `browser` | `chrome` | Browser (Chrome is currently the only supported one) |
| `headless` | `false` | Run Chrome without a window |
| `timeout.seconds` | `10` | Explicit wait timeout |
| `slow.motion.ms` | `0` | Pause before each click, type and text read, to follow a run by eye |
| `user.standard.username` | `standard_user` | Username of the standard user |
| `user.password` | `secret_sauce` | Password shared by all users |

Every key can be overridden, in this order of precedence:

1. System property: `./gradlew test -Dheadless=true`
2. Environment variable, upper-cased with dots as underscores: `USER_PASSWORD=... ./gradlew test`
3. `config.properties`

Examples:

```bash
./gradlew test -Dheadless=true                    # run without a browser window
./gradlew test -Dslow.motion.ms=500               # slow motion
```

In IntelliJ, add the same `-D` options to the run configuration's VM options.

### Credentials

The credentials in `config.properties` are the public demo values shown on the SauceDemo login page, kept
there so the project runs straight after cloning. In a real project they would be removed from the file and
injected as environment variables (`USER_STANDARD_USERNAME`, `USER_PASSWORD`) from CI secrets.
The password is never written to the Allure results.

## Project structure

```
src/main/java/com/swaglabs/
├── config/ConfigReader.java       # resolves settings: system property > env variable > config.properties
├── driver/DriverFactory.java      # creates and quits Chrome; one driver per thread (ThreadLocal)
└── pages/                         # page objects
    ├── BasePage.java              # explicit waits and common actions shared by all pages
    ├── LoginPage.java
    ├── InventoryPage.java
    ├── CartPage.java
    ├── CheckoutInformationPage.java
    ├── CheckoutOverviewPage.java
    └── CheckoutCompletePage.java
src/main/resources/config.properties

src/test/java/com/swaglabs/
├── base/BaseTest.java             # opens a fresh browser before each test and quits it after
├── data/TestData.java             # test data and expected messages
├── listeners/TestListener.java    # attaches a screenshot to Allure when a test fails
└── tests/                         # LoginTests, EndToEndTests
```

## Design notes

- **Page Object Model.** Pages hold locators and actions only. All assertions are in the tests.
- **Page-changing actions return the next page**, e.g. `loginAs(...)` returns `InventoryPage`. Actions that
  are expected to fail stay on the same page (`loginExpectingError(...)`, `continueExpectingError()`).
- **Stable locators.** `data-test` attributes and element ids, no XPath.
- **Explicit waits only.** Every action in `BasePage` waits for its element; there are no fixed sleeps
  (apart from the opt-in slow motion, which is 0 by default).
- **Independent tests.** Each test gets a new browser, and the driver is held per thread, so tests could run in parallel.