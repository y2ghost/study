package study.ywork.selenium.api.javascript;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import java.util.Set;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScriptKey;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class PinnedScriptsTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testPinnedScripts() {
        String initPage = "http://localhost:8080/";
        driver.get(initPage);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        ScriptKey linkKey = js.pin("return document.getElementsByTagName('a')[2];");
        ScriptKey firstArgKey = js.pin("return arguments[0];");
        Set<ScriptKey> pinnedScripts = js.getPinnedScripts();
        assertThat(pinnedScripts).hasSize(2);

        WebElement formLink = (WebElement) js.executeScript(linkKey);
        formLink.click();
        assertThat(driver.getCurrentUrl()).isNotEqualTo(initPage);
        String message = "自动化测试你好呀!";
        String executeScript = (String) js.executeScript(firstArgKey, message);
        assertThat(executeScript).isEqualTo(message);

        js.unpin(linkKey);
        assertThat(js.getPinnedScripts()).hasSize(1);
    }
}
