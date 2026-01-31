package study.ywork.selenium.remote.remote;

import static io.github.bonigarcia.wdm.WebDriverManager.isOnline;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class RemoteEdgeTest {
    private WebDriver driver;

    @BeforeClass
    void setupClass() throws MalformedURLException {
        URL seleniumServerUrl = new URL("http://localhost:4444/");
        Optional<Path> browserPath = WebDriverManager.edgedriver().remoteAddress(seleniumServerUrl).getBrowserPath();
        assumeThat(browserPath.isPresent()).isTrue();
        WebDriverManager.edgedriver().setup();
    }

    @BeforeMethod
    void setup() throws MalformedURLException {
        URL seleniumServerUrl = new URL("http://localhost:4444/");
        assumeThat(isOnline(seleniumServerUrl)).isTrue();
        driver = RemoteWebDriver.builder().oneOf(new EdgeOptions()).address(seleniumServerUrl).build();
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
