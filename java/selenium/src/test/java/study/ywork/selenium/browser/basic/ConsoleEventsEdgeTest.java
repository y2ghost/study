package study.ywork.selenium.browser.basic;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.events.CdpEventTypes;
import org.openqa.selenium.logging.HasLogEvents;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ConsoleEventsEdgeTest {
    static final Logger log = getLogger(lookup().lookupClass());
    private WebDriver driver;

    @BeforeClass
    void setupClass() {
        Optional<Path> browserPath = WebDriverManager.edgedriver().getBrowserPath();
        assumeThat(browserPath.isPresent()).isTrue();
    }

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.edgedriver().create();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testConsoleEvents() throws InterruptedException {
        HasLogEvents logger = (HasLogEvents) driver;
        CountDownLatch latch = new CountDownLatch(4);
        logger.onLogEvent(CdpEventTypes.consoleEvent(consoleEvent -> {
            log.debug("{} {}: {}", consoleEvent.getTimestamp(), consoleEvent.getType(), consoleEvent.getMessages());
            latch.countDown();
        }));
        driver.get("http://localhost:8080/console-logs.html");
        assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
    }
}
