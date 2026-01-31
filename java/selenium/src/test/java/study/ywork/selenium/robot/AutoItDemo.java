package study.ywork.selenium.robot;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.firefox.FirefoxDriver;

public class AutoItDemo {

    public static void main(String[] args) throws IOException, InterruptedException {

        WebDriver driver = new FirefoxDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));

        Runtime.getRuntime().exec("AutoItTest.exe");

        driver.get("https://www.example.com");

        Thread.sleep(5000);

        driver.close();
    }
}