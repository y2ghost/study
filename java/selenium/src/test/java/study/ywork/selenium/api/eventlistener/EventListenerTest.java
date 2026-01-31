package study.ywork.selenium.api.eventlistener;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class EventListenerTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        MyEventListener listener = new MyEventListener();
        WebDriver originalDriver = WebDriverManager.chromedriver().create();
        driver = new EventFiringDecorator<>(listener).decorate(originalDriver);
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testEventListener() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).isEqualTo("YY学习Selenium");
        driver.findElement(By.linkText("Web表单")).click();
    }
}
