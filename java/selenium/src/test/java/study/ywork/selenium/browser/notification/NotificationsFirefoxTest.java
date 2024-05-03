package study.ywork.selenium.browser.notification;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NotificationsFirefoxTest {
    static final Logger log = getLogger(lookup().lookupClass());
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("permissions.default.desktop-notification", 1);
        driver = new FirefoxDriver(options);
    }

    @AfterMethod
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testNotifications() {
        driver.get("http://localhost:8080/notifications.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = String.join("\n", "const callback = arguments[arguments.length - 1];",
                "const OldNotify = window.Notification;", "function newNotification(title, options) {",
                "    callback(title);", "    return new OldNotify(title, options);", "}",
                "newNotification.requestPermission = OldNotify.requestPermission.bind(OldNotify);",
                "Object.defineProperty(newNotification, 'permission', {", "    get: function() {",
                "        return OldNotify.permission;", "    }", "});", "window.Notification = newNotification;",
                "document.getElementById('notify-me').click();");
        log.debug("Executing the following script asynchronously:\n{}", script);
        Object notificationTitle = js.executeAsyncScript(script);
        assertThat(notificationTitle).isEqualTo("这是一个通知");
    }
}
