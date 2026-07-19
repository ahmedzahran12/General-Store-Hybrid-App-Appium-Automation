package test_cases.android_tests;

import android_pages.CartPage;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import android_pages.HomePage;
import test_utils.AndroidBaseTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EndToEndTest extends AndroidBaseTest {
    @Test(dataProvider = "checkoutFlowDataProvider")
    public void testCheckoutFlow(HashMap<String, String> myData) throws InterruptedException {
        List<String> productsToBeAdded = new ArrayList<>();
        productsToBeAdded.add(myData.get("product1"));
        productsToBeAdded.add(myData.get("product2"));
        HomePage homePage = new HomePage(getDriver());
        homePage.selectCountry(myData.get("country"))
        .enterName(myData.get("name"))
        .selectGender(myData.get("gender"))
        .goToProductsPage()
        .addProductsToCart(productsToBeAdded, myData.get("targetText"))
        .goToCart();
        CartPage cartPage = new CartPage(getDriver());
        Assert.assertEquals(cartPage.getActualTotalPrice(), cartPage.getExpectedTotalPrice());
        Assert.assertEquals(cartPage.getCartProductsNames(), productsToBeAdded);
        Assert.assertEquals(cartPage.longClickTermsBtnAndGetTextThenClose(), "Terms Of Conditions");
        cartPage.clickCheckBox().clickCompletePurchaseBtn();
//        waitForContext();
//        getDriver().context("WEBVIEW_com.androidsample.generalstore");
//        getDriver().findElement(By.name("q")).sendKeys("checkoutpage.com");
//        getDriver().findElement(By.name("q")).sendKeys(Keys.ENTER);
//        getDriver().pressKey(new KeyEvent(AndroidKey.ENTER));

    }

    @DataProvider(name = "checkoutFlowDataProvider")
    public Object[][] checkoutFlowDataProvider() throws IOException {
        String path = System.getProperty("user.dir") + "//src//test//java//test_data//E2E-test-data.json";
        List<HashMap<String, String>> data = getJsonData(path);
        Object[][] dataProvider = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            dataProvider[i][0] = data.get(i);
        }

        return dataProvider;
    }
}
