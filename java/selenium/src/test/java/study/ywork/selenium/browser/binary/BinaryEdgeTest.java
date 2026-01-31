package study.ywork.selenium.browser.binary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BinaryEdgeTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        Path browserBinary = Paths.get("/Applications/Microsoft Edge Canary.app/Contents/MacOS/Microsoft Edge Canary");
        assumeThat(browserBinary).exists();

        EdgeOptions options = new EdgeOptions();
        options.setBinary(browserBinary.toFile());
        driver = WebDriverManager.edgedriver().capabilities(options).create();
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
