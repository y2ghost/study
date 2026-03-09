package study.ywork.selenium.browser.l10n;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class AcceptLangChromeTest {
    private WebDriver driver;
    private String lang;

    @BeforeMethod
    void setup() {
        lang = "en-US";
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", lang);
        options.setExperimentalOption("prefs", prefs);
        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testAcceptLang() {
        driver.get("http://localhost:8080/multilanguage.html");
        ResourceBundle strings = ResourceBundle.getBundle("strings", Locale.forLanguageTag(lang));
        String home = strings.getString("home");
        String content = strings.getString("content");
        String about = strings.getString("about");
        String contact = strings.getString("contact");
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).contains(home).contains(content).contains(about).contains(contact);
    }
}
