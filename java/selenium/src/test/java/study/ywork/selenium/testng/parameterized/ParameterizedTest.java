package study.ywork.selenium.testng.parameterized;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ParameterizedTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @DataProvider(name = "loginData")
    public static Object[][] data() {
        return new Object[][] { { "user", "user", "登陆成功" }, { "bad-user", "bad-passwd", "无效凭证" } };
    }

    @Test(dataProvider = "loginData")
    void testParameterized(String username, String password, String expectedText) {
        driver.get("http://localhost:8080/login-form.html");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button")).click();
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).contains(expectedText);
    }
}
