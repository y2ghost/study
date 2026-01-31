package study.ywork.selenium.integration.download;

import java.io.File;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DownloadFirefoxTest {
    private WebDriver driver;
    private File targetFolder;

    @BeforeMethod
    void setup() {
        FirefoxOptions options = new FirefoxOptions();
        targetFolder = new File(".");
        options.addPreference("browser.download.dir", targetFolder.getAbsolutePath());
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "image/png, application/pdf");
        options.addPreference("pdfjs.disabled", true);
        driver = new FirefoxDriver(options);
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testDownloadFirefox() {
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
