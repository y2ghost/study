package study.ywork.selenium.browser.basic;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.network.Network;
import org.openqa.selenium.devtools.v119.network.model.Cookie;
import org.openqa.selenium.devtools.v119.storage.Storage;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ManageCookiesTest {
    static final Logger log = getLogger(lookup().lookupClass());
    private WebDriver driver;
    private DevTools devTools;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
        devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        devTools.close();
        driver.quit();
    }

    @Test
    void testManageCookies() {
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        driver.get("http://localhost:8080/cookies.html");

        // 读取cookies
        List<Cookie> cookies = devTools.send(Storage.getCookies(null));
        cookies.forEach(cookie -> log.debug("{}={}", cookie.getName(), cookie.getValue()));
        List<String> cookieName = cookies.stream().map(cookie -> cookie.getName()).sorted()
                .collect(Collectors.toList());
        Set<org.openqa.selenium.Cookie> seleniumCookie = driver.manage().getCookies();
        List<String> selCookieName = seleniumCookie.stream().map(selCookie -> selCookie.getName()).sorted()
                .collect(Collectors.toList());
        assertThat(cookieName).isEqualTo(selCookieName);

        // 清除cookies
        devTools.send(Network.clearBrowserCookies());
        List<Cookie> cookiesAfterClearing = devTools.send(Storage.getCookies(null));
        assertThat(cookiesAfterClearing).isEmpty();

        driver.findElement(By.id("refresh-cookies")).click();
    }
}
