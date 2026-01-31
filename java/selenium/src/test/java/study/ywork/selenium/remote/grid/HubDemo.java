package study.ywork.selenium.remote.grid;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class HubDemo {
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        FirefoxOptions chromeOptions = new FirefoxOptions();
        WebDriver driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444"), chromeOptions);
        driver.get("https://www.baidu.com");
        Thread.sleep(5000);
        driver.quit();
    }
}
