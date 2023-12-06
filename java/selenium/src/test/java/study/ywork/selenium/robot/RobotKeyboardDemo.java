package study.ywork.selenium.robot;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RobotKeyboardDemo {
    public static void main(String[] args) throws InterruptedException, AWTException {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://demoqa.com/keyboard-events/");
        driver.manage().window().maximize();
        Thread.sleep(2000);
        WebElement webElement = driver.findElement(By.id("browseFile"));
        webElement.sendKeys(Keys.ENTER);
        // Robot类的使用方法
        Robot robot = new Robot();
        // 输入 D1.txt
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_1);
        robot.keyPress(KeyEvent.VK_PERIOD);
        robot.keyPress(KeyEvent.VK_T);
        robot.keyPress(KeyEvent.VK_X);
        robot.keyPress(KeyEvent.VK_T);
        robot.keyPress(KeyEvent.VK_ENTER);
        Thread.sleep(1000);
        // 验证组件
        webElement = driver.findElement(By.id("uploadButton"));
        webElement.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert myAlert = wait.until(ExpectedConditions.alertIsPresent());
        myAlert.accept();
        driver.close();
    }
}
