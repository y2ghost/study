package study.ywork.selenium.browser.iemode;

import static java.lang.invoke.MethodHandles.lookup;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class IEmodeEdgeTest {
    static final Logger log = getLogger(lookup().lookupClass());
    private WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        assumeThat(IS_OS_WINDOWS).isTrue();
        WebDriverManager.iedriver().setup();
    }

    @BeforeMethod
    void setup() {
        Optional<Path> browserPath = WebDriverManager.edgedriver().getBrowserPath();
        assumeThat(browserPath).isPresent();
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.attachToEdgeChrome();
        options.withEdgeExecutablePath(browserPath.get().toString());
        driver = new InternetExplorerDriver(options);
    }

    @AfterMethod
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testIEmodeEdge() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
