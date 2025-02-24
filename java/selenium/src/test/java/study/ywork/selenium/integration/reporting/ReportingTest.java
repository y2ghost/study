package study.ywork.selenium.integration.reporting;

import static org.assertj.core.api.Assertions.assertThat;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ReportingTest {
    private WebDriver driver;
    private ExtentReports reports;

    @BeforeClass
    void setupClass() {
        reports = new ExtentReports();
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("extentReport.html");
        reports.attachReporter(htmlReporter);
    }

    @BeforeMethod
    void setup(Method method) {
        reports.createTest(method.getName());
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @AfterClass
    void teardownClass() {
        reports.flush();
    }

    @Test
    void testReporting1() {
        checkIndex(driver);
    }

    @Test
    void testReporting2() {
        checkIndex(driver);
    }

    void checkIndex(WebDriver driver) {
        driver.get("http://localhost:8080/");
        assertThat(driver.getTitle()).contains("Selenium");
    }
}
