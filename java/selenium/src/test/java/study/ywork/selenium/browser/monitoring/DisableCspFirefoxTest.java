package study.ywork.selenium.browser.monitoring;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

@Ignore
public class DisableCspFirefoxTest {
    private WebDriverManager wdm = WebDriverManager.firefoxdriver().watch().disableCsp();
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = wdm.create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testDisableCspFirefox() {
        driver.get("https://www.baidu.com/");
        List<Map<String, Object>> logMessages = wdm.getLogs();
        assertThat(logMessages).isNotNull();
    }
}
