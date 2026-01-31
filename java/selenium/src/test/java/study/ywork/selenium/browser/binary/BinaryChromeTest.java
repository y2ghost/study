package study.ywork.selenium.browser.binary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BinaryChromeTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        Path browserBinary = Paths.get("/usr/bin/google-chrome-beta");
        assumeThat(browserBinary).exists();
        ChromeOptions options = new ChromeOptions();
        options.setBinary(browserBinary.toFile());
        driver = WebDriverManager.chromedriver().capabilities(options).create();
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
