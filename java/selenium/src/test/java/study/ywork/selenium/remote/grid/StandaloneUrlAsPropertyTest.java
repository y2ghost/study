package study.ywork.selenium.remote.grid;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class StandaloneUrlAsPropertyTest {
    private WebDriver driver;

    @BeforeClass
    void setupAll() {
        int port = PortProber.findFreePort();
        WebDriverManager.chromedriver().setup();
        Main.main(new String[] { "standalone", "--port", String.valueOf(port) });
        System.setProperty("webdriver.remote.server", String.format("http://localhost:%d/", port));
    }

    @BeforeMethod
    void setup() {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(options);
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @AfterClass
    void teardownClass() {
        System.clearProperty("webdriver.remote.server");
    }

    @Test
    void testStandaloneUrlAsProperty() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
