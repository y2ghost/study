package study.ywork.selenium.browser.basic;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.dom.model.Rect;
import org.openqa.selenium.devtools.v119.page.Page;
import org.openqa.selenium.devtools.v119.page.Page.GetLayoutMetricsResponse;
import org.openqa.selenium.devtools.v119.page.model.Viewport;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class FullPageScreenshotChromeTest {
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
    void testFullPageScreenshotChrome() throws IOException {
        driver.get("http://localhost:8080/long-page.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.className("container"), By.tagName("p")));

        GetLayoutMetricsResponse metrics = devTools.send(Page.getLayoutMetrics());
        Rect contentSize = metrics.getContentSize();
        String screenshotBase64 = devTools.send(Page.captureScreenshot(Optional.empty(), Optional.empty(),
                Optional.of(new Viewport(0, 0, contentSize.getWidth(), contentSize.getHeight(), 1)), Optional.empty(),
                Optional.of(true), java.util.Optional.empty()));
        Path destination = Paths.get("fullpage-screenshot-chrome.png");
        Files.write(destination, Base64.getDecoder().decode(screenshotBase64));
        assertThat(destination).exists();
    }
}
