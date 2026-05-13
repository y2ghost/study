package study.ywork.selenium.testng.browser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CrossBrowserTest {
    private WebDriver driver;

    @DataProvider(name = "browsers")
    public static Object[][] data() {
        return new Object[][] { { "chrome" }, { "edge" }, { "firefox" } };
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test(dataProvider = "browsers")
    void testCrossBrowser(String browserName) {
        WebDriverManager wdm = WebDriverManager.getInstance(browserName);
        Optional<Path> browserPath = wdm.getBrowserPath();
        assumeThat(browserPath.isPresent()).isTrue();
        driver = wdm.create();
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
