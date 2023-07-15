package study.ywork.selenium.api.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BasicMethodTest {
    private WebDriver driver;
    private static final Logger log = getLogger(BasicMethodTest.class);

    @BeforeMethod
    void setup() {
        // 可以配置条件浏览器驱动，比如不存在chrome时使用safari浏览器
        driver = RemoteWebDriver.builder().oneOf(new ChromeOptions()).addAlternative(new SafariOptions()).build();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testBasicMethods() {
        String testUrl = "http://localhost:8080/";
        driver.get(testUrl);
        String title = driver.getTitle();
        log.debug("The title of {} is {}", testUrl, title);
        assertThat(title).isEqualTo("YY学习Selenium");
        assertThat(driver.getCurrentUrl()).isEqualTo(testUrl);
        assertThat(driver.getPageSource()).containsIgnoringCase("</html>");
    }
}
