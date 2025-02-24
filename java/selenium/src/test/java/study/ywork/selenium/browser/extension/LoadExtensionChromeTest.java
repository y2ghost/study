package study.ywork.selenium.browser.extension;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoadExtensionChromeTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() throws URISyntaxException {
        Path extension = Paths.get(ClassLoader.getSystemResource("web-extension").toURI());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--load-extension=" + extension.toAbsolutePath().toString());
        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testExtensions() {
        driver.get("http://localhost:8080/");
        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertThat(h1.getText()).isNotEqualTo("YY学习Selenium");
    }
}
