package study.ywork.selenium.pom.test;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class VanillaBasicLoginTest {
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
    void testVanillaBasicLoginSuccess() {
        driver.get("http://localhost:8080/login-form.html");
        driver.findElement(By.id("username")).sendKeys("user");
        driver.findElement(By.id("password")).sendKeys("user");
        driver.findElement(By.cssSelector("button")).click();
        assertThat(driver.findElement(By.id("success")).isDisplayed()).isTrue();
    }

    @Test
    void testVanillaBasicLoginFailure() {
        driver.get("http://localhost:8080/login-form.html");
        driver.findElement(By.id("username")).sendKeys("bad-user");
        driver.findElement(By.id("password")).sendKeys("bad-password");
        driver.findElement(By.cssSelector("button")).click();
        assertThat(driver.findElement(By.id("invalid")).isDisplayed()).isTrue();
    }
}
