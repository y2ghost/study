package study.ywork.selenium.browser.incognito;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class IncognitoChromeTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testIncognito() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
