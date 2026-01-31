package study.ywork.selenium.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HtmlUnitDriverTest {
	private WebDriver driver;

	@BeforeMethod
	void setup() {
		driver = new HtmlUnitDriver();
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
