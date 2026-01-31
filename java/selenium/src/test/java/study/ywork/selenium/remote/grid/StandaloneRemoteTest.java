package study.ywork.selenium.remote.grid;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class StandaloneRemoteTest {
    private WebDriver driver;
    private URL seleniumServerUrl;

    @BeforeClass
    void setupAll() throws MalformedURLException {
        int port = PortProber.findFreePort();
        WebDriverManager.chromedriver().setup();
        Main.main(new String[] { "standalone", "--port", String.valueOf(port) });
        seleniumServerUrl = new URL(String.format("http://localhost:%d/", port));
    }

    @BeforeMethod
    void setup() {
        driver = new RemoteWebDriver(seleniumServerUrl, new ChromeOptions());
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testStandaloneRemote() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
