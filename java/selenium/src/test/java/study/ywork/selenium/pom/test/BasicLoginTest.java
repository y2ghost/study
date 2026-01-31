package study.ywork.selenium.pom.test;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import study.ywork.selenium.pom.page.BasicLoginPage;

public class BasicLoginTest {
    private WebDriver driver;
    private BasicLoginPage login;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
        login = new BasicLoginPage(driver);
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testBasicLoginSuccess() {
        login.with("user", "user");
        assertThat(login.successBoxPresent()).isTrue();
    }
}
