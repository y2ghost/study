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
import org.openqa.selenium.devtools.v119.network.model.BlockedReason;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.google.common.collect.ImmutableList;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BlockUrlTest {
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
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        devTools.close();
        driver.quit();
    }

    @Test
    void testBlockUrl() {
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        String urlToBlock = "http://localhost:8080/img/yy-icon.png";
        devTools.send(Network.setBlockedURLs(ImmutableList.of(urlToBlock)));

        devTools.addListener(Network.loadingFailed(), loadingFailed -> {
            BlockedReason reason = loadingFailed.getBlockedReason().get();
            log.debug("Blocking reason: {}", reason);
            assertThat(reason).isEqualTo(BlockedReason.INSPECTOR);
        });

        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
