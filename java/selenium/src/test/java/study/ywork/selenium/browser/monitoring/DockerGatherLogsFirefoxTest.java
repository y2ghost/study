package study.ywork.selenium.browser.monitoring;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

@Ignore
public class DockerGatherLogsFirefoxTest {
    static final Logger log = getLogger(lookup().lookupClass());
    private WebDriverManager wdm = WebDriverManager.firefoxdriver().browserInDocker().watch();
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
    void testDockerGatherLogsFirefox() {
        driver.get("http://localhost:8080/console-logs.html");
        List<Map<String, Object>> logMessages = wdm.getLogs();
        assertThat(logMessages).hasSize(5);
        logMessages.forEach(map -> log.debug("[{}] [{}] {}", map.get("datetime"),
                String.format("%1$-14s",
                        map.get("source").toString().toUpperCase() + "." + map.get("type").toString().toUpperCase()),
                map.get("message")));
    }
}
