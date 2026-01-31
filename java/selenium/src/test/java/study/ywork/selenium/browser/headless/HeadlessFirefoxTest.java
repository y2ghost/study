package study.ywork.selenium.browser.headless;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HeadlessFirefoxTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless=new");
        driver = new FirefoxDriver(options);
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
