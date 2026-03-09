package study.ywork.selenium.browser.extension;

import static io.github.bonigarcia.wdm.WebDriverManager.zipFolder;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoadExtensionFirefoxTest {
    private WebDriver driver;
    private Path zippedExtension;

    @BeforeMethod
    void setup() throws URISyntaxException {
        Path extensionFolder = Paths.get(ClassLoader.getSystemResource("web-extension").toURI());
        zippedExtension = zipFolder(extensionFolder);
        driver = new FirefoxDriver();
        ((FirefoxDriver) driver).installExtension(zippedExtension, true);
    }

    @AfterMethod
    void teardown() throws InterruptedException, IOException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        Files.delete(zippedExtension);
        driver.quit();
    }

    @Test
    void testExtensions() {
        driver.get("http://localhost:8080/");
        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertThat(h1.getText()).isNotEqualTo("YY学习Selenium");
    }
}
