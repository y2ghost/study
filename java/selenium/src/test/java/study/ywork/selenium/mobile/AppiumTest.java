package study.ywork.selenium.mobile;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;

public class AppiumTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() throws MalformedURLException {
        URL appiumServerUrl = new URL("http://localhost:4723");
        assumeThat(isOnline(new URL(appiumServerUrl, "/status"))).isTrue();
        ChromeOptions options = new ChromeOptions();
        options.setCapability("platformName", "Android");
        options.setCapability("deviceName", "Nexus 5 API 30");
        options.setCapability("automationName", "UiAutomator2");
        driver = new AppiumDriver(appiumServerUrl, options);
    }

    @AfterMethod
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testAppium() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
