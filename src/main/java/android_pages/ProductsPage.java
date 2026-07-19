package android_pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.AndroidActions;

import java.util.List;

public class ProductsPage extends AndroidActions {
    AndroidDriver driver;
    private final By productPageTitle = By.xpath("//*[@text='Products']");
    private final By productTitles = By.xpath("//android.widget.TextView[@resource-id='com.androidsample.generalstore:id/productName']");
    private final By addToCartButtons = By.xpath("//android.widget.TextView[@text='ADD TO CART']");
    private final By productsList = By.id("com.androidsample.generalstore:id/rvProductList");
    private final By cartButton = By.id("com.androidsample.generalstore:id/appbar_btn_cart");
    public ProductsPage(AndroidDriver driver){
        super(driver);
        this.driver=driver;
    }
    public ProductsPage addProductsToCart(List<String> desiredProducts, String targetText) {
        waitForElementToAppear(productsList,driver);

        for(String product : desiredProducts) {
            controlledScrollToText(product, driver.findElement(productsList));
            By targetAddToCart = By.xpath("//*[@text='" + product + "']/..//*[@text='" + targetText + "']");
            while(driver.findElements(targetAddToCart).isEmpty() || !driver.findElement(targetAddToCart).isDisplayed()) {
                scrollForward();
            }
            driver.findElement(targetAddToCart).click();
        }
        return this;
    }
    public String getPageTitle() {
        waitForElementToAppear(productPageTitle,driver);
       return driver.findElement(productPageTitle).getText();
    }
    public CartPage goToCart(){
        driver.findElement(cartButton).click();
        return new CartPage(driver);
    }


}
