package study.ywork.selenium.testng.category;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CategoriesTest {
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod(alwaysRun = true)
    void teardown() {
        driver.quit();
    }

    @Test(groups = { "WebForm" })
    void testCategoriesWebForm() {
        driver.get("http://localhost:8080/web-form.html");
        assertThat(driver.getCurrentUrl()).contains("web-form");
    }

    @Test(groups = { "HomePage" })
    void tesCategoriestHomePage() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getCurrentUrl()).doesNotContain("web-form");
    }
}
