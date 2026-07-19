package android_pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.AndroidActions;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends AndroidActions {
    AndroidDriver driver;
    private final By cartPageTitle = By.xpath("//*[@text='Cart']");
    private final By cartProductsNames = By.xpath("//android.widget.TextView[@resource-id='com.androidsample.generalstore:id/productName']");
    private final By productPrices = By.xpath("//android.widget.TextView[@resource-id='com.androidsample.generalstore:id/productPrice']");
    private final By totalPurchaseAmount = By.id("com.androidsample.generalstore:id/totalAmountLbl");
    private final By checkBox = AppiumBy.className("android.widget.CheckBox");
    private final By completePurchaseBtn = By.id("com.androidsample.generalstore:id/btnProceed");
    private final By termsBtn = By.id("com.androidsample.generalstore:id/termsButton");
    private final By closeTermsBtn = By.id("android:id/button1");
    private final By termsText = By.id("com.androidsample.generalstore:id/alertTitle");
    public CartPage(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public List<String> getCartProductsNames() {
        waitForElementToAppear(cartPageTitle,driver);
        List<WebElement> productNames = driver.findElements(cartProductsNames);
        return productNames.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public double getExpectedTotalPrice(){
        double sum = 0;
        List<WebElement> prices = driver.findElements(productPrices);
        for(WebElement price : prices){
            double formattedPrice = getFormattedAmount(price.getText());
            sum = sum + formattedPrice;
        }
        return sum;
    }
    public double getActualTotalPrice(){
        waitForElementToAppear(totalPurchaseAmount,driver);
        return getFormattedAmount(driver.findElement(totalPurchaseAmount).getText());

    }
    public CartPage clickCheckBox(){
        driver.findElement(checkBox).click();
        return this;
    }
    public void clickCompletePurchaseBtn(){
        driver.findElement(completePurchaseBtn).click();
    }
    public String longClickTermsBtnAndGetTextThenClose(){
        longClick(driver.findElement(termsBtn));
        waitForElementToAppear(closeTermsBtn,driver);
        String text = driver.findElement(termsText).getText();
        driver.findElement(closeTermsBtn).click();
        return text;
    }

}
