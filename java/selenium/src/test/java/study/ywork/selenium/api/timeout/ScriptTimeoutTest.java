package study.ywork.selenium.api.timeout;

import static org.testng.Assert.assertThrows;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ScriptTimeoutTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testScriptTimeout() {
        driver.get("http://localhost:8080/");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(3));
        assertThrows(ScriptTimeoutException.class, () -> {
            long waitMillis = Duration.ofSeconds(5).toMillis();
            String script = "const callback = arguments[arguments.length - 1];"
                    + "window.setTimeout(callback, "
                    + waitMillis + ");";
            js.executeAsyncScript(script);
        });
    }
}
