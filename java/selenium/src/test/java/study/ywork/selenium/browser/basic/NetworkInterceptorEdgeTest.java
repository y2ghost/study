package study.ywork.selenium.browser.basic;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class NetworkInterceptorEdgeTest {
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
    void testNetworkInterceptor() throws Exception {
        Path img = Paths.get(ClassLoader.getSystemResource("tools.png").toURI());
        byte[] bytes = Files.readAllBytes(img);

        try (NetworkInterceptor interceptor = new NetworkInterceptor(driver,
                Route.matching(req -> req.getUri().endsWith(".png"))
                        .to(() -> req -> new HttpResponse().setContent(Contents.bytes(bytes))))) {
            driver.get("http://localhost:8080/");

            int width = Integer.parseInt(driver.findElement(By.tagName("img")).getAttribute("width"));
            assertThat(width).isGreaterThan(80);
        }
    }
}
