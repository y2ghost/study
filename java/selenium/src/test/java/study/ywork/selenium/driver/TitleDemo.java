package study.ywork.selenium.driver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

public class TitleDemo {
	private static final Logger log = getLogger(TitleDemo.class);

	public static void testTitle(WebDriver driver) {
		String testUrl = "http://localhost:8080/";
		driver.get(testUrl);
		String title = driver.getTitle();
		log.debug("网页{}的标题是{}", testUrl, title);
		assertThat(title).isEqualTo("YY学习Selenium");
	}
}
