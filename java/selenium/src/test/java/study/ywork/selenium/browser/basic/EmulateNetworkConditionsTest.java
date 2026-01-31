package study.ywork.selenium.browser.basic;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.time.Duration;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.network.Network;
import org.openqa.selenium.devtools.v119.network.model.ConnectionType;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class EmulateNetworkConditionsTest {
    static final Logger log = getLogger(lookup().lookupClass());
    private WebDriver driver;
    private DevTools devTools;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
        devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();
    }

    @AfterMethod
    void teardown() {
        devTools.close();
        driver.quit();
    }

    @Test
    void testEmulateNetworkConditions() {
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.emulateNetworkConditions(false, 100, 50 * 1024, 50 * 1024,
                Optional.of(ConnectionType.CELLULAR3G)));
        long initMillis = System.currentTimeMillis();
        driver.get("http://localhost:8080/");
        Duration elapsed = Duration.ofMillis(System.currentTimeMillis() - initMillis);
        log.debug("页面加载时间花费 {} ms", elapsed.toMillis());
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
