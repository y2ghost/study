package study.ywork.selenium.testng.failure;

import static org.assertj.core.api.Assertions.fail;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

@Ignore("注释我以便验证测试失败")
public class FailureTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            FailureManager failureManager = new FailureManager(driver);
            failureManager.takePngScreenshot(result.getName());
        }

        driver.quit();
    }

    @Test
    void testFailure() {
        driver.get("http://localhost:8080/");
        fail("测试失败的情况");
    }
}
