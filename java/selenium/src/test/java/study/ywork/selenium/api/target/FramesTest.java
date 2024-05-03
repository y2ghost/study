package study.ywork.selenium.api.target;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class FramesTest {
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
    void testFrames() {
        driver.get("http://localhost:8080/frames.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String frameName = "frame-body";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(frameName)));
        driver.switchTo().frame(frameName);

        By pName = By.tagName("p");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pName, 0));
        List<WebElement> paragraphs = driver.findElements(pName);
        assertThat(paragraphs).hasSize(8);
    }
}
