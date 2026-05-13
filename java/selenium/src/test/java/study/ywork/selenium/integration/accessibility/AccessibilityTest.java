package study.ywork.selenium.integration.accessibility;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.deque.html.axecore.selenium.AxeReporter;
import io.github.bonigarcia.wdm.WebDriverManager;

public class AccessibilityTest {
    final Logger log = getLogger(lookup().lookupClass());
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testAccessibility() {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
        Results result = new AxeBuilder().analyze(driver);
        List<Rule> violations = result.getViolations();
        violations.forEach(rule -> {
            log.debug("{}", rule.toString());
        });
        AxeReporter.writeResultsToJsonFile("testAccessibility", result);
    }
}
