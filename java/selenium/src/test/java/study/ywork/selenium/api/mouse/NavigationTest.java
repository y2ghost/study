package study.ywork.selenium.api.mouse;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class NavigationTest {
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
    void testNavigation() {
        driver.get("http://localhost:8080/");
        driver.findElement(By.xpath("//a[text()='导航页面']")).click();
        driver.findElement(By.xpath("//a[text()='下一页']")).click();
        driver.findElement(By.xpath("//a[text()='3']")).click();
        driver.findElement(By.xpath("//a[text()='2']")).click();
        driver.findElement(By.xpath("//a[text()='上一页']")).click();
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).contains("自动化测试的小和尚");
    }
}
