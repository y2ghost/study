package study.ywork.selenium.testng.order;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class OrderTest {
    private WebDriver driver;

    @BeforeClass
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterClass
    void teardown() {
        driver.quit();
    }

    @Test(priority = 1)
    void testA() {
        driver.get("http://localhost:8080/navigation1.html");
        assertBodyContains("小和尚");
    }

    @Test(priority = 2)
    void testB() {
        driver.findElement(By.linkText("2")).click();
        assertBodyContains("挑水喝");
    }

    @Test(priority = 3)
    void testC() {
        driver.findElement(By.linkText("3")).click();
        assertBodyContains("修行方式");
    }

    void assertBodyContains(String text) {
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).contains(text);
    }
}
