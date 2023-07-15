package study.ywork.selenium.integration.performance;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class HarCreatorTest {
    private WebDriver driver;
    private BrowserMobProxy proxy;

    @BeforeMethod
    void setup() {
        proxy = new BrowserMobProxyServer();
        proxy.start();
        proxy.newHar();
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        ChromeOptions options = new ChromeOptions();
        options.setProxy(seleniumProxy);
        options.setAcceptInsecureCerts(true);
        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    @AfterMethod
    void teardown() throws IOException {
        Har har = proxy.getHar();
        File harFile = new File("login.har");
        har.writeTo(harFile);
        proxy.stop();
        driver.quit();
    }

    @Test
    void testHarCreator() {
        driver.get("http://localhost:8080/login-form.html");
        driver.findElement(By.id("username")).sendKeys("user");
        driver.findElement(By.id("password")).sendKeys("user");
        driver.findElement(By.cssSelector("button")).click();
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).contains("登陆成功");
    }
}
