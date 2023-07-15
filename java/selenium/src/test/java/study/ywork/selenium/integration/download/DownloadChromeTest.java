package study.ywork.selenium.integration.download;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DownloadChromeTest {
    private WebDriver driver;
    private File targetFolder;

    @BeforeMethod
    void setup() {
        targetFolder = new File(System.getProperty("user.home"), "Downloads");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", targetFolder.toString());
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testDownloadChrome() {
        driver.get("http://localhost:8080/download.html");
        driver.findElement(By.xpath("(//a)[2]")).click();
        driver.findElement(By.xpath("(//a)[3]")).click();
        ConditionFactory await = Awaitility.await().atMost(Duration.ofSeconds(5));
        File wdmLogo = new File(targetFolder, "webdrivermanager.png");
        await.until(() -> wdmLogo.exists());
        File wdmDoc = new File(targetFolder, "webdrivermanager.pdf");
        await.until(() -> wdmDoc.exists());
    }
}
