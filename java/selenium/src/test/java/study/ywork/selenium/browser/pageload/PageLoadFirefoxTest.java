package study.ywork.selenium.browser.pageload;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.time.Duration;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PageLoadFirefoxTest {
    static final Logger log = getLogger(lookup().lookupClass());
    private WebDriver driver;
    private PageLoadStrategy pageLoadStrategy;

    @BeforeMethod
    void setup() {
        FirefoxOptions options = new FirefoxOptions();
        pageLoadStrategy = PageLoadStrategy.EAGER;
        options.setPageLoadStrategy(pageLoadStrategy);
        driver = new FirefoxDriver(options);
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testPageLoad() {
        long initMillis = System.currentTimeMillis();
        driver.get("http://localhost:8080/");
        Duration elapsed = Duration.ofMillis(System.currentTimeMillis() - initMillis);
        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        Object pageLoad = capabilities.getCapability(CapabilityType.PAGE_LOAD_STRATEGY);
        String browserName = capabilities.getBrowserName();
        log.debug("页面加载时间:{} ms, 加载策略: {}, 浏览器: {}", elapsed.toMillis(), pageLoad, browserName);
        assertThat(pageLoad).isEqualTo(pageLoadStrategy.toString());
    }
}
