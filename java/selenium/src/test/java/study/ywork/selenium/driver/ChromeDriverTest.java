package study.ywork.selenium.driver;

import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ChromeDriverTest {
	private WebDriver driver;

	@BeforeClass
	void setupClass() {
		Optional<Path> browserPath = WebDriverManager.chromedriver().getBrowserPath();
		assumeThat(browserPath.isPresent()).isTrue();
		WebDriverManager.chromedriver().setup();
	}

	@BeforeMethod
	void setup() {
		driver = new ChromeDriver();
	}

	@AfterMethod
	void teardown() {
		driver.quit();
	}

	@Test
	void test() {
		TitleDemo.testTitle(driver);
	}
}
