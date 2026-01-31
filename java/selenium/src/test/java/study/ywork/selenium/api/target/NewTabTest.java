package study.ywork.selenium.api.target;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class NewTabTest {
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
    void testNewTab() {
        driver.get("http://localhost:8080/");
        String initHandle = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("http://localhost:8080/web-form.html");
        assertThat(driver.getWindowHandles()).hasSize(2);
        driver.switchTo().window(initHandle);
        driver.close();
        assertThat(driver.getWindowHandles()).hasSize(1);
    }
}
