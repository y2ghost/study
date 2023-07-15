package study.ywork.selenium.api.wait;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ExplicitWaitTest {
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
    void testExplicitWait() {
        driver.get("http://localhost:8080/loading-images.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement landscape = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("landscape")));
        assertThat(landscape.getAttribute("src")).containsIgnoringCase("landscape");
    }
}
