package study.ywork.selenium.browser.location;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LocationContextTest {
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
    void testLocationContext() {
        LocationContext location = (LocationContext) driver;
        location.setLocation(new Location(27.5916, 86.5640, 8850));
        driver.get("http://localhost:8080/geolocation.html");
        driver.findElement(By.id("get-coordinates")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement coordinates = driver.findElement(By.id("coordinates"));
        wait.until(ExpectedConditions.visibilityOf(coordinates));
    }
}
