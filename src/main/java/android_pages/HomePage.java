package android_pages;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import utils.AndroidActions;

public class HomePage extends AndroidActions {
    AndroidDriver driver;
    private final By countryList = By.id("com.androidsample.generalstore:id/spinnerCountry");
    private final By nameField = By.id("com.androidsample.generalstore:id/nameField");
    private final By maleGender = By.id("com.androidsample.generalstore:id/radioMale");
    private final By femaleGender = By.id("com.androidsample.generalstore:id/radioFemale");
    private final By shopButton = By.id("com.androidsample.generalstore:id/btnLetsShop");
    private final By scrollableList = AppiumBy.className("android.widget.ListView");
    private final By toastMsg = By.xpath("(//android.widget.Toast)[1]");

    public HomePage(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public HomePage selectCountry(String country) {
        waitForElementToAppear(countryList, driver);
        driver.findElement(countryList).click();
        waitForElementToAppear(scrollableList, driver);
        scrollToText(country);
        driver.findElement(By.xpath("//android.widget.TextView[@text='" + country + "']")).click();
        return this;
    }

    public HomePage enterName(String name) {
        driver.findElement(nameField).sendKeys(name);
        return this;
    }

    public HomePage selectGender(String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            driver.findElement(maleGender).click();
        } else if (gender.equalsIgnoreCase("Female")) {
            driver.findElement(femaleGender).click();
        }
        return this;
    }

    public ProductsPage goToProductsPage() {
        driver.findElement(shopButton).click();
        return new ProductsPage(driver);
    }

    public String getToastMsgText() {
        waitForElementToBePresent(toastMsg, driver);
        return driver.findElement(toastMsg).getAttribute("name");
    }

    public void setActivity() {
        String activity = "com.androidsample.generalstore/com.androidsample.generalstore.MainActivity";
        driver.executeScript("mobile: startActivity", ImmutableMap.of("intent", activity));
    }
}
