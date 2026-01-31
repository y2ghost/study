package study.ywork.selenium.browser.extension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoadExtensionEdgeTest {
    private WebDriver driver;

    @BeforeClass
    void setupClass() {
        Optional<Path> browserPath = WebDriverManager.edgedriver().getBrowserPath();
        assumeThat(browserPath.isPresent()).isTrue();
    }

    @BeforeMethod
    void setup() throws URISyntaxException {
        Path extension = Paths.get(ClassLoader.getSystemResource("web-extension").toURI());
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--load-extension=" + extension.toAbsolutePath().toString());
        driver = WebDriverManager.edgedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testExtensions() {
        driver.get("http://localhost:8080/");
        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertThat(h1.getText()).isNotEqualTo("YY学习Selenium");
    }
}
