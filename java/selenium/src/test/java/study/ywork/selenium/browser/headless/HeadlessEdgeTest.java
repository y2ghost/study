package study.ywork.selenium.browser.headless;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.nio.file.Path;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class HeadlessEdgeTest {
    private WebDriver driver;

    @BeforeClass
    void setupClass() {
        Optional<Path> browserPath = WebDriverManager.edgedriver().getBrowserPath();
        assumeThat(browserPath.isPresent()).isTrue();
        WebDriverManager.edgedriver().setup();
    }

    @BeforeMethod
    void setup() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless=new");
        driver = RemoteWebDriver.builder().oneOf(options).build();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testHeadless() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
