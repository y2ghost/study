package study.ywork.selenium.testng.disabled;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DisabledTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Ignore("暂时不测试")
    @Test
    void testDisabled1() {
        checkPracticeSite();
    }

    @Test(enabled = false)
    void testDisabled2() {
        checkPracticeSite();
    }

    @Test
    private void checkPracticeSite() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
