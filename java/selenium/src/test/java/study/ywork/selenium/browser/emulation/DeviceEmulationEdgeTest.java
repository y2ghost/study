package study.ywork.selenium.browser.emulation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DeviceEmulationEdgeTest {
    private WebDriver driver;

    @BeforeClass
    void setupClass() {
        Optional<Path> browserPath = WebDriverManager.edgedriver().getBrowserPath();
        assumeThat(browserPath.isPresent()).isTrue();
    }

    @BeforeMethod
    void setup() {
        EdgeOptions options = new EdgeOptions();
        Map<String, Object> mobileEmulation = new HashMap<>();
        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 360);
        deviceMetrics.put("height", 640);
        deviceMetrics.put("pixelRatio", 3.0);
        deviceMetrics.put("touch", true);
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) "
                + "AppleWebKit/535.19 (KHTML, like Gecko) " + "Chrome/18.0.1025.166 Mobile Safari/535.19");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        driver = WebDriverManager.edgedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testDeviceEmulation() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
