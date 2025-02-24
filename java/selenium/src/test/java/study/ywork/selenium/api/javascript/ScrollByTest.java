package study.ywork.selenium.api.javascript;

import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ScrollByTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testScrollBy() {
        driver.get("http://localhost:8080/long-page.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "window.scrollBy(0, 1000);";
        js.executeScript(script);
    }
}
