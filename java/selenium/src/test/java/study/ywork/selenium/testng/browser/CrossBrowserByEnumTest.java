package study.ywork.selenium.testng.browser;

import static io.github.bonigarcia.wdm.config.DriverManagerType.CHROME;
import static io.github.bonigarcia.wdm.config.DriverManagerType.EDGE;
import static io.github.bonigarcia.wdm.config.DriverManagerType.FIREFOX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;

public class CrossBrowserByEnumTest {
    private WebDriver driver;

    @DataProvider(name = "browsers")
    public static Object[][] data() {
        return new Object[][] { { CHROME }, { EDGE }, { FIREFOX } };
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test(dataProvider = "browsers")
    void testCrossBrowserByEnum(DriverManagerType driverManagerType) {
        WebDriverManager wdm = WebDriverManager.getInstance(driverManagerType);
        Optional<Path> browserPath = wdm.getBrowserPath();
        assumeThat(browserPath.isPresent()).isTrue();
        driver = wdm.create();
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
