package study.ywork.selenium.integration.performance;

import static io.github.bonigarcia.wdm.WebDriverManager.isDockerAvailable;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoadTest {
    static final int NUM_BROWSERS = 5;
    final Logger log = getLogger(lookup().lookupClass());
    private List<WebDriver> driverList;
    private WebDriverManager wdm = WebDriverManager.chromedriver().browserInDocker();

    @BeforeMethod
    void setupTest() {
        assumeThat(isDockerAvailable()).isTrue();
        driverList = wdm.create(NUM_BROWSERS);
    }

    @AfterMethod
    void teardown() {
        wdm.quit();
    }

    @Test
    void testLoad() throws InterruptedException {
        ExecutorService executorService = newFixedThreadPool(NUM_BROWSERS);
        CountDownLatch latch = new CountDownLatch(NUM_BROWSERS);

        driverList.forEach((driver) -> {
            executorService.submit(() -> {
                try {
                    checkHomePage(driver);
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await(60, SECONDS);
        executorService.shutdown();
    }

    void checkHomePage(WebDriver driver) {
        log.debug("Session id {}", ((RemoteWebDriver) driver).getSessionId());
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
