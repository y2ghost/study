package study.ywork.selenium.browser.basic;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FullPageScreenshotFirefoxTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = new FirefoxDriver();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testFullPageScreenshotFirefox() throws IOException {
        driver.get("http://localhost:8080/long-page.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.className("container"), By.tagName("p")));

        byte[] imageBytes = ((FirefoxDriver) driver).getFullPageScreenshotAs(OutputType.BYTES);
        Path destination = Paths.get("fullpage-screenshot-firefox.png");
        Files.write(destination, imageBytes);
        assertThat(destination).exists();
    }
}
