package study.ywork.selenium.driver;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.openqa.selenium.net.PortProber.findFreePort;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ChromiumDriverTest {
	private WebDriver driver;
	private static Optional<Path> browserPath;

	@BeforeClass
	void setupClass() {
		browserPath = WebDriverManager.chromiumdriver().getBrowserPath();
		assumeThat(browserPath).isPresent();
		WebDriverManager.chromiumdriver().setup();
	}

	@BeforeMethod
	void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-debugging-port=" + findFreePort());
		options.setBinary(browserPath.get().toFile());
		driver = new ChromeDriver(options);
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
