package study.ywork.selenium.browser.headless;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class HeadlessChromeTest {
    private WebDriver driver;

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testHeadless() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
