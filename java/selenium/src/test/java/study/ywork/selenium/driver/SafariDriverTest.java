package study.ywork.selenium.driver;

import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SafariDriverTest {
	private WebDriver driver;

	@BeforeClass
	void setupClass() {
		Optional<Path> browserPath = WebDriverManager.safaridriver().getBrowserPath();
		assumeThat(browserPath).isPresent();
	}

	@BeforeMethod
	void setup() {
		driver = new SafariDriver();
	}

	@AfterTest
	void teardown() {
		driver.quit();
	}

	@Test
	void test() {
		TitleDemo.testTitle(driver);
	}
}
