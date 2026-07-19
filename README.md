#  General Store – Hybrid App Appium Automation

A professional **mobile test automation framework** built with **Java + Appium** for the *General Store* Android hybrid application. The framework follows the **Page Object Model (POM)** design pattern and supports both **local device/emulator** execution and **cloud-based testing via BrowserStack**.

---

##  Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Architecture](#project-architecture)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Test Cases](#test-cases)
- [Test Data](#test-data)
- [Running Tests](#running-tests)
- [Reporting](#reporting)
- [BrowserStack Integration](#browserstack-integration)
- [Project Structure](#project-structure)

---

## Overview

This framework automates end-to-end functional tests for the **General Store** Android hybrid app — a shopping app that combines native UI screens with embedded WebViews. The suite covers the complete customer journey:

- Filling in the registration/home form (name, country, gender)
- Browsing and adding products to the cart
- Verifying cart totals and product lists
- Accepting terms & conditions via long-press gesture
- Completing the purchase checkout flow

The framework is designed with **parallelism**, **thread-safety**, and **CI-readiness** in mind, using `ThreadLocal` driver management and a TestNG listener-based reporting system.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| **Java** | 21 | Primary language |
| **Appium Java Client** | 10.1.1 | Mobile automation driver |
| **TestNG** | 7.12.0 | Test runner and assertions |
| **ExtentReports** | 5.1.2 | HTML test reporting |
| **Jackson Databind** | 2.15.2 | JSON test data parsing |
| **Apache Commons IO** | 2.22.0 | File I/O (screenshots, config) |
| **BrowserStack Java SDK** | LATEST | Cloud execution |
| **Maven** | 3.x | Build and dependency management |

---

## Project Architecture

The framework implements the **Page Object Model (POM)** pattern with a clean layered inheritance hierarchy:

```
AppiumUtils  (base utilities: waits, screenshots, JSON reading)
    +-- AndroidActions  (Android-specific gestures: scroll, swipe, long-click)
            +-- HomePage        (registration form page)
            +-- ProductsPage    (product listing & add-to-cart)
            +-- CartPage        (cart validation & checkout)
```

### Design Principles

- **Fluent Interface / Method Chaining** — Page object methods return `this` (or the next page object), enabling clean, readable test chains:

```java
homePage.selectCountry("Egypt")
        .enterName("Ahmed Zahran")
        .selectGender("Male")
        .goToProductsPage()
        .addProductsToCart(products, "ADD TO CART")
        .goToCart();
```

- **Thread-Safe Driver Management** — `AndroidBaseTest` uses `ThreadLocal<AndroidDriver>` and `ThreadLocal<AppiumDriverLocalService>` to support parallel test execution without driver conflicts.
- **Data-Driven Testing** — All test inputs are externalised to JSON files and fed into tests via TestNG `@DataProvider`.
- **Automatic Appium Server Management** — The base test class programmatically starts and stops the Appium server via `AppiumServiceBuilder` — no manual server launch needed.

---

## Prerequisites

Ensure the following are installed and configured before running the framework:

1. **Java JDK 21+** — https://adoptium.net/
2. **Apache Maven 3.x** — https://maven.apache.org/download.cgi
3. **Node.js & npm** — Required for Appium server
4. **Appium 2.x** — Install via npm:
   ```bash
   npm install -g appium
   appium driver install uiautomator2
   ```
5. **Android SDK** — With `ANDROID_HOME` environment variable set
6. **ADB (Android Debug Bridge)** — Included in Android SDK platform-tools
7. **An Android Emulator or a Physical Device** — Emulator must be running before tests start
8. **ChromeDriver** — Required for WebView context switching; version must match the Chrome version bundled in the app
9. **General Store APK** — The app under test (`.apk` file)

---

## Configuration

All environment-specific settings are stored in `src/main/java/resources/config.properties`:

```properties
ipAddress=127.0.0.1
appiumJSPath=C://Users//YourUser//AppData//Roaming//npm//node_modules//appium//build//lib//main.js
appPath=D://path//to//General-Store.apk
deviceName=YourEmulatorName
chromedriverExecutable=C://path//to//chromedriver.exe
```

| Property | Description |
|---|---|
| `ipAddress` | IP address for the Appium server (default: `127.0.0.1`) |
| `appiumJSPath` | Absolute path to the Appium `main.js` entry point |
| `appPath` | Absolute path to the General Store `.apk` file |
| `deviceName` | Name of your AVD (Android Virtual Device) or connected device |
| `chromedriverExecutable` | Absolute path to `chromedriver.exe` (for hybrid WebView support) |

> **Note:** The `ipAddress` property can also be overridden at runtime via a Maven system property:
> `mvn test -DipAddress=192.168.1.10`

---

## Test Cases

### HomePageTest — Registration Form Validation

| Test Method | Description |
|---|---|
| `testValidFormSubmission` | Fills in a valid country, name, and gender, submits the form, and asserts the user lands on the **Products** page |
| `testSubmittingFormWithEmptyNameField` | Submits the form without entering a name and asserts the Toast message `"Please enter your name"` is displayed |

Both tests are data-driven via `form-test-data.json` and use `@AfterMethod` to reset the app back to `MainActivity` after each run.

---

### EndToEndTest — Full Checkout Flow

| Test Method | Description |
|---|---|
| `testCheckoutFlow` | Complete E2E journey: fills registration form ? selects products ? validates cart total matches sum of individual prices ? validates cart product list ? long-presses Terms & Conditions button and verifies the dialog title ? accepts terms ? proceeds to checkout |

**Assertions performed:**
- Actual total amount displayed equals the computed sum of individual product prices
- Products in the cart match the list of products added
- Long-pressing the Terms button opens the dialog with title `"Terms Of Conditions"`

---

## Test Data

Test inputs are stored as JSON arrays in `src/test/java/test_data/`:

**`form-test-data.json`** — Used by `HomePageTest`:
```json
[
  {
    "country": "Egypt",
    "name": "Ahmed Zahran",
    "gender": "Male"
  }
]
```

**`E2E-test-data.json`** — Used by `EndToEndTest`:
```json
[
  {
    "product1": "Jordan 6 Rings",
    "product2": "Nike SFB Jungle",
    "country": "Egypt",
    "name": "Ahmed Zahran",
    "gender": "Male",
    "targetText": "ADD TO CART"
  }
]
```

To add new test scenarios, simply append additional JSON objects to these files. TestNG's `@DataProvider` will automatically pick them up and run the test once per object.

---

## Running Tests

### Option 1 — IntelliJ IDEA (Recommended)

Open the project, right-click `testng.xml` ? **Run**.

### Option 2 — Maven CLI

Run all tests in the configured suite:
```bash
mvn test
```

Override the Appium IP address at runtime:
```bash
mvn test -DipAddress=127.0.0.1
```

### Option 3 — TestNG XML Suite directly

Point your runner at:
```
src/test/test_suites/testng.xml
```

The suite runs the following test classes in order:
1. `EndToEndTest` ? `testCheckoutFlow`
2. `HomePageTest` ? `testValidFormSubmission`, `testSubmittingFormWithEmptyNameField`

---

## Reporting

Test results are automatically generated as a rich **HTML report** via **ExtentReports (Spark Reporter)**.

After a test run, open the report at:
```
<project-root>/reports/index.html
```

The report includes:
- Pass / Fail / Skip status per test method
- Full stack trace on failures
- **Automatic screenshots** captured on test failure and embedded directly in the report

Screenshot capture is handled by `Listeners.java`, a `ITestListener` registered in TestNG that hooks into `onTestFailure`. Screenshots are saved to `<project-root>/reports/<testMethodName>.png`.

---

## BrowserStack Integration

The framework is fully integrated with **BrowserStack App Automate** for cloud-based execution on real devices.

Configuration is managed via `browserstack.yml`:

```yaml
userName: <your-browserstack-username>
accessKey: <your-browserstack-access-key>

projectName: Tests on Cloud
buildName: browserstack build

app: bs://<your-app-id>   # Upload your APK to BrowserStack first

platforms:
  - platformName: android
    deviceName: Google Pixel 9
    platformVersion: '17.0'
    deviceOrientation: portrait

parallelsPerPlatform: 3
browserstackLocal: true
```

### Steps to Run on BrowserStack

1. Upload the APK to BrowserStack and retrieve the `bs://` App URL
2. Set the `app` field in `browserstack.yml`
3. Set your `userName` and `accessKey`
4. Run: `mvn test -P browserstack`

Parallel execution is controlled by `parallelsPerPlatform`. With the current configuration of `3`, up to 3 test threads run simultaneously against the same device.

> **`browserstackLocal: true`** enables a secure tunnel, allowing BrowserStack to access apps and services that are not publicly accessible over the internet.

---

## Project Structure

```
General-Store-Hybrid-App-Appium-Automation/
¦
+-- browserstack.yml                        # BrowserStack cloud config
+-- pom.xml                                 # Maven dependencies and build config
¦
+-- src/
    +-- main/
    ¦   +-- java/
    ¦       +-- android_pages/              # Page Object Model classes
    ¦       ¦   +-- HomePage.java           # Registration/home screen
    ¦       ¦   +-- ProductsPage.java       # Product listing screen
    ¦       ¦   +-- CartPage.java           # Shopping cart screen
    ¦       ¦
    ¦       +-- utils/                      # Reusable utility classes
    ¦       ¦   +-- AppiumUtils.java        # Waits, screenshots, JSON reader
    ¦       ¦   +-- AndroidActions.java     # Gestures (scroll, swipe, long-click)
    ¦       ¦   +-- ConfigReader.java       # config.properties reader
    ¦       ¦
    ¦       +-- resources/
    ¦           +-- config.properties       # Local environment configuration
    ¦
    +-- test/
        +-- java/
        ¦   +-- test_cases/
        ¦   ¦   +-- android_tests/
        ¦   ¦       +-- EndToEndTest.java   # Full checkout flow test
        ¦   ¦       +-- HomePageTest.java   # Form validation tests
        ¦   ¦
        ¦   +-- test_data/
        ¦   ¦   +-- E2E-test-data.json      # Data for checkout flow
        ¦   ¦   +-- form-test-data.json     # Data for form tests
        ¦   ¦
        ¦   +-- test_utils/
        ¦       +-- AndroidBaseTest.java    # Base class: driver lifecycle management
        ¦       +-- ExtentReportConfig.java # Extent HTML reporter setup
        ¦       +-- Listeners.java          # TestNG listener: reporting and screenshots
        ¦
        +-- test_suites/
            +-- testng.xml                  # TestNG suite definition
```

---

## Key Framework Features at a Glance

| Feature | Implementation |
|---|---|
| Page Object Model | `android_pages/` package |
| Fluent API / Method Chaining | Page methods return `this` or next page object |
| Thread-safe parallel execution | `ThreadLocal<AndroidDriver>` in `AndroidBaseTest` |
| Automatic Appium server startup | `AppiumServiceBuilder` in `AndroidBaseTest` |
| Data-driven tests | JSON `@DataProvider` via Jackson Databind |
| Hybrid app (WebView) support | `waitForContext()` + `driver.context()` switching |
| Failure screenshots | `Listeners.onTestFailure()` + `AppiumUtils.getScreenshotPath()` |
| HTML reporting | ExtentReports Spark Reporter |
| Cloud execution | BrowserStack SDK + `browserstack.yml` |
| Controlled scrolling | `UiScrollable` + `mobile:scrollGesture` W3C actions |
