package study.ywork.selenium.robot;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * 
 * InputEvent.BUTTON1_DOWN_MASK : 鼠标左键 InputEvent.BUTTON2_DOWN_MASK : 鼠标中建
 * InputEvent.BUTTON3_DOWN_MASK : 鼠标右键 InputEvent.BUTTON1_MASK
 * InputEvent.BUTTON2_MASK InputEvent.BUTTON3_MASK mouseRelease(int buttons):
 * 释放鼠标键 mouseMove(int x, int y): 鼠标移动
 */
public class RobotMouseDemo {
    public static void main(String[] args) throws InterruptedException, AWTException, IOException {
        WebDriver driver = new FirefoxDriver();
        String URL = "https://demoqa.com/keyboard-events/";
        driver.manage().window().maximize();
        driver.get(URL);
        Thread.sleep(2000);
        WebElement webElement = driver.findElement(By.id("browseFile"));
        webElement.sendKeys("ENTER");
        Robot robot = new Robot();
        Dimension i = driver.manage().window().getSize();
        System.out.println("Dimension x and y :" + i.getWidth() + " " + i.getHeight());
        int x = (i.getWidth() / 4) + 20;
        int y = (i.getHeight() / 10) + 50;
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        System.out.println("Browse button clicked");
        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_ENTER);
        System.out.println("Closed the windows popup");
        Thread.sleep(1000);
        driver.close();
    }
}