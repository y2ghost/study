package study.ywork.selenium.integration.fakedata;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.datafaker.Faker;

public class FakeDataTest {
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
    void testFakeData() {
        driver.get("http://localhost:8080/data-types.html");
        Faker faker = new Faker();
        driver.findElement(By.name("first-name")).sendKeys(faker.name().firstName());
        driver.findElement(By.name("last-name")).sendKeys(faker.name().lastName());
        driver.findElement(By.name("address")).sendKeys(faker.address().fullAddress());
        driver.findElement(By.name("zip-code")).sendKeys(faker.address().zipCode());
        driver.findElement(By.name("city")).sendKeys(faker.address().city());
        driver.findElement(By.name("country")).sendKeys(faker.address().country());
        driver.findElement(By.name("e-mail")).sendKeys(faker.internet().emailAddress());
        driver.findElement(By.name("phone")).sendKeys(faker.phoneNumber().phoneNumber());
        driver.findElement(By.name("job-position")).sendKeys(faker.job().position());
        driver.findElement(By.name("company")).sendKeys(faker.company().name());
        driver.findElement(By.tagName("form")).submit();
        List<WebElement> successElement = driver.findElements(By.className("alert-success"));
        assertThat(successElement).hasSize(10);
        List<WebElement> errorElement = driver.findElements(By.className("alert-danger"));
        assertThat(errorElement).isEmpty();
    }
}
