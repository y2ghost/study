package study.ywork.selenium.browser.basic;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.performance.Performance;
import org.openqa.selenium.devtools.v119.performance.model.Metric;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class PerformanceMetricsTest {
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
    void testPerformanceMetrics() {
        devTools.send(Performance.enable(Optional.empty()));
        driver.get("http://localhost:8080/");

        List<Metric> metrics = devTools.send(Performance.getMetrics());
        assertThat(metrics).isNotEmpty();
        metrics.forEach(metric -> log.debug("{}: {}", metric.getName(), metric.getValue()));
    }
}
