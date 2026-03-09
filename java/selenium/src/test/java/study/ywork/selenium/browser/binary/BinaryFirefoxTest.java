package study.ywork.selenium.browser.binary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BinaryFirefoxTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        Path browserBinary = Paths.get("/Applications/Firefox.app/Contents/MacOS/firefox");
        assumeThat(browserBinary).exists();

        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(browserBinary);
        driver = new FirefoxDriver(options);
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());

        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testBinary() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
