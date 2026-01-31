package study.ywork.selenium.api.locator;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ByAllTest {
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
    void testByAll() {
        driver.get("http://localhost:8080/web-form.html");
        List<WebElement> rowsInForm = driver.findElements(new ByAll(By.tagName("form"), By.className("row")));
        assertThat(rowsInForm).hasSize(5);
    }
}
