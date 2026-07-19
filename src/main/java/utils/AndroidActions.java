package utils;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class AndroidActions extends AppiumUtils {
    AndroidDriver driver;

    public AndroidActions(AndroidDriver driver) {

        this.driver = driver;
    }

    public void scrollToText(String text) {
        driver.findElement(AppiumBy
                .androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"" + text + "\"))"));
    }

    public void scrollToTextFromParent(String anchorText, String targetText) {
        String elementSelector = "new UiSelector().text(\"" + anchorText + "\")"
                + ".fromParent(new UiSelector().text(\"" + targetText + "\"))";
        String scrollCommand = "new UiScrollable(new UiSelector().scrollable(true))" + ".scrollIntoView("
                + elementSelector + ")";
    }
    public void scrollForward(){
        driver.findElement(AppiumBy.androidUIAutomator( "new UiScrollable(new UiSelector().scrollable(true).instance(0)) .scrollForward()"));
    }
    public void controlledScrollToText(String text,WebElement scrollableElement){
        boolean canScrollMore = true;
        while(canScrollMore){
            try{
                if(driver.findElement(AppiumBy.xpath("//*[@text='"+text+"']")).isDisplayed()) {
                    break;
                }
            }
            catch (NoSuchElementException e){
                canScrollMore = (boolean)driver.executeScript("mobile:scrollGesture",ImmutableMap.of(
                "elementId",((RemoteWebElement)scrollableElement).getId(),
                "direction","down",
                "percent",0.1));
            }
        }

    }

    public void longClick(WebElement element) {
        driver.executeScript("mobile: longClickGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(),
                "duration", 2000));
    }

    public void swipeInDirection(WebElement element, String direction, double percent) {
        driver.executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(),
                "direction", direction,
                "percent", percent));
    }
}
