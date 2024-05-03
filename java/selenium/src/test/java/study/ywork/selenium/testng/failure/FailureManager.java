package study.ywork.selenium.testng.failure;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

public class FailureManager {
    static final Logger log = getLogger(lookup().lookupClass());
    private WebDriver driver;

    public FailureManager(WebDriver driver) {
        this.driver = driver;
    }

    void takePngScreenshot(String filename) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenshot = ts.getScreenshotAs(OutputType.FILE);
        Path destination = Paths.get(filename + ".png");

        try {
            Files.move(screenshot.toPath(), destination);
        } catch (IOException e) {
            log.error("移动文件{}到{}出现异常", screenshot, destination, e);
        }
    }
}
