package study.ywork.selenium.browser.extension;

import static org.openqa.selenium.support.ui.ExpectedConditions.attributeToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class AddExtensionChromeTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() throws URISyntaxException {
        Path extension = Paths.get(ClassLoader.getSystemResource("dark-bg.crx").toURI());
        ChromeOptions options = new ChromeOptions();
        options.addExtensions(extension.toFile());
        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testAddExtension() {
        driver.get("http://localhost:8080/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement body = driver.findElement(By.tagName("body"));
        String whiteRgba = "rgba(255, 255, 255, 1)";
        wait.until(not(attributeToBe(body, "background-color", whiteRgba)));
    }
}
