package study.ywork.selenium.api.downdown;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SelectTest {
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
    void test() {
        driver.get("http://localhost:8080/web-form.html");
        Select select = new Select(driver.findElement(By.name("my-select")));
        String optionLabel = "Three";
        select.selectByVisibleText(optionLabel);
        assertThat(select.getFirstSelectedOption().getText()).isEqualTo(optionLabel);
    }
}
