package study.ywork.selenium.api.cookie;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class AddCookiesTest {
    private WebDriver driver;

    @BeforeClass
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    void setup() {
        driver = new ChromeDriver();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        // 留点时间用于手工检查
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testAddCookies() {
        driver.get("http://localhost:8080/cookies.html");
        Cookie newCookie = new Cookie("new-cookie-key", "new-cookie-value");
        driver.manage().addCookie(newCookie);
        String readValue = driver.manage().getCookieNamed(newCookie.getName()).getValue();
        assertThat(newCookie.getValue()).isEqualTo(readValue);
        driver.findElement(By.id("refresh-cookies")).click();
    }
}
