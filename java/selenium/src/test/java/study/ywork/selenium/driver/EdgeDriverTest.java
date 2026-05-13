package study.ywork.selenium.driver;

import static org.assertj.core.api.Assumptions.assumeThat;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class EdgeDriverTest {
	private WebDriver driver;

	@BeforeClass
	void setupClass() {
		Optional<Path> browserPath = WebDriverManager.edgedriver().getBrowserPath();
		assumeThat(browserPath.isPresent()).isTrue();
		WebDriverManager.edgedriver().setup();
	}

	@BeforeMethod
	void setup() {
		driver = new EdgeDriver();
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
