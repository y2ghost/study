package study.ywork.selenium.remote.remote;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RemoteChromeTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() throws MalformedURLException {
        URL seleniumServerUrl = new URL("http://localhost:4444/");
        assumeThat(isOnline(seleniumServerUrl)).isTrue();
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(seleniumServerUrl, options);
    }

    @AfterMethod
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testRemote() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
