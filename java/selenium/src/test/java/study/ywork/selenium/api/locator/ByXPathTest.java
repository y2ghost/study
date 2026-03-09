package study.ywork.selenium.api.locator;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ByXPathTest {
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
    void teardown() {
        driver.quit();
    }

    @Test
    void testByXPathBasic() {
        driver.get("http://localhost:8080/web-form.html");
        WebElement hidden = driver.findElement(By.xpath("//input[@type='hidden']"));
        assertThat(hidden.isDisplayed()).isFalse();
    }

    @Test
    void testByXPathAdvanced() {
        driver.get("http://localhost:8080/web-form.html");

        WebElement radio1 = driver.findElement(By.xpath("//*[@type='radio' and @checked]"));
        assertThat(radio1.getAttribute("id")).isEqualTo("my-radio-1");
        assertThat(radio1.isSelected()).isTrue();

        WebElement radio2 = driver.findElement(By.xpath("//*[@type='radio' and not(@checked)]"));
        assertThat(radio2.getAttribute("id")).isEqualTo("my-radio-2");
        assertThat(radio2.isSelected()).isFalse();
    }
}
