package study.ywork.selenium.browser.monitoring;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class RecordEdgeTest {
    static final Logger log = getLogger(lookup().lookupClass());
    static final int REC_TIMEOUT_SEC = 10;
    static final int POLL_TIME_MSEC = 100;
    static final String REC_FILENAME = "myRecordingEdge";
    static final String REC_EXT = ".webm";
    private WebDriver driver;
    private File targetFolder;
    private WebDriverManager wdm = WebDriverManager.edgedriver().watch();

    @BeforeClass
    void setupClass() {
        Optional<Path> browserPath = WebDriverManager.edgedriver().getBrowserPath();
        assumeThat(browserPath.isPresent()).isTrue();
    }

    @BeforeMethod
    void setup() {
        driver = wdm.create();
        targetFolder = new File(System.getProperty("user.home"), "Downloads");
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testRecordEdge() throws InterruptedException {
        driver.get("http://localhost:8080/slow-calculator.html");
        wdm.startRecording(REC_FILENAME);
        // 计算: 1 + 3
        driver.findElement(By.xpath("//span[text()='1']")).click();
        driver.findElement(By.xpath("//span[text()='+']")).click();
        driver.findElement(By.xpath("//span[text()='3']")).click();
        driver.findElement(By.xpath("//span[text()='=']")).click();

        // 等待结果
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBe(By.className("screen"), "4"));
        wdm.stopRecording();
        long timeoutMs = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(REC_TIMEOUT_SEC);

        File recFile;
        do {
            recFile = new File(targetFolder, REC_FILENAME + REC_EXT);
            if (System.currentTimeMillis() > timeoutMs) {
                fail("录制超时" + REC_TIMEOUT_SEC + "秒!" + recFile);
                break;
            }
            Thread.sleep(POLL_TIME_MSEC);

        } while (!recFile.exists());

        log.debug("录制文件路径 {}", recFile);
    }
}
