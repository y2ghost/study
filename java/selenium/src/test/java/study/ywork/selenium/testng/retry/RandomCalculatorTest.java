package study.ywork.selenium.testng.retry;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class RandomCalculatorTest {
    private WebDriver driver;

    @BeforeClass
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterClass
    void teardown() {
        driver.quit();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    void testRandomCalculator() {
        driver.get("http://localhost:8080/random-calculator.html");
        // 计算: 1 + 3
        driver.findElement(By.xpath("//span[text()='1']")).click();
        driver.findElement(By.xpath("//span[text()='+']")).click();
        driver.findElement(By.xpath("//span[text()='3']")).click();
        driver.findElement(By.xpath("//span[text()='=']")).click();
        // 获取结果
        String result = driver.findElement(By.className("screen")).getText();
        assertThat(result).isEqualTo("4");
    }
}
