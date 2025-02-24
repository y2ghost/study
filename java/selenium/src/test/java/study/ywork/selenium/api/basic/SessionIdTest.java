package study.ywork.selenium.api.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SessionIdTest {
	private static final Logger log = getLogger(SessionIdTest.class);
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
	void testSessionId() {
		driver.get("http://localhost:8080/");
		SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
		assertThat(sessionId).isNotNull();
		log.debug("The sessionId is {}", sessionId.toString());
	}
}
