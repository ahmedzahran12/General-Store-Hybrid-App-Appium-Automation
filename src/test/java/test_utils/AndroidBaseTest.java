package test_utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.AppiumUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class AndroidBaseTest extends AppiumUtils {
    private static final ThreadLocal<AppiumDriverLocalService> service = new ThreadLocal<>();
    private static final ThreadLocal<AndroidDriver> driver = new ThreadLocal<>();

    @BeforeClass
    public void setUpService() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "\\src\\main\\java\\resources\\config.properties");
        prop.load(fis);

        String ipAddress = System.getProperty("ipAddress") != null ? System.getProperty("ipAddress")
                : prop.getProperty("ipAddress");
        String appiumJSPath = prop.getProperty("appiumJSPath");
        String appPath = prop.getProperty("appPath");
        String deviceName = prop.getProperty("deviceName");
        String chromedriverExecutable = prop.getProperty("chromedriverExecutable");

        AppiumDriverLocalService localService = new AppiumServiceBuilder()
                .withAppiumJS(new File(appiumJSPath))
                .withIPAddress(ipAddress)
                .usingAnyFreePort()
                .build();
        localService.start();
        service.set(localService);
        UiAutomator2Options options = new UiAutomator2Options();
        options.setApp(appPath);
        options.setDeviceName(deviceName);
        options.setChromedriverExecutable(chromedriverExecutable);
        options.setCapability("browserstack.clearSystemFiles", "true");
        int systemPort = 8200 + (int) (Math.random() * 100);
        options.setSystemPort(systemPort);
        driver.set(new AndroidDriver(service.get().getUrl(), options));
    }

    public AndroidDriver getDriver() {
        return driver.get();
    }

    public void waitForContext() {
        while (true) {
            Set<String> contexts = getDriver().getContextHandles();
            if (contexts.size() > 1) {
                break;
            }
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
        if (service.get() != null) {
            service.get().stop();
            service.remove();
        }
    }
}
