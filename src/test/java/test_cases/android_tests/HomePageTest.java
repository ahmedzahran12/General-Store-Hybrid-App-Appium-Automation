package test_cases.android_tests;

import android_pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test_utils.AndroidBaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class HomePageTest extends AndroidBaseTest {
    @AfterMethod
    public void setup() {
        new HomePage(getDriver()).setActivity();
    }

    @Test(dataProvider = "getData")
    public void testValidFormSubmission(HashMap<String, String> input) {
        String title = new HomePage(getDriver())
                .selectCountry(input.get("country"))
                .enterName(input.get("name"))
                .selectGender(input.get("gender"))
                .goToProductsPage()
                .getPageTitle();
        Assert.assertEquals(title, "Products");
    }

    @Test(dataProvider = "getData")
    public void testSubmittingFormWithEmptyNameField(HashMap<String, String> input) {
        HomePage homePage = new HomePage(getDriver());
        homePage
                .selectCountry(input.get("country"))
                .selectGender(input.get("gender"))
                .goToProductsPage();
        Assert.assertEquals(homePage.getToastMsgText(), "Please enter your name");
    }

    @DataProvider
    public Object[][] getData() throws IOException {
        List<HashMap<String, String>> data = getJsonData(
                System.getProperty("user.dir") + "\\src\\test\\java\\test_data\\form-test-data.json");
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }
}
