package study.ywork.selenium.robot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * BotStyleTests风格示例
 */
public class ActionBot {
    private final WebDriver driver;

    public ActionBot(WebDriver driver) {
        this.driver = driver;
    }

    public void click(By locator) {
        driver.findElement(locator).click();
    }

    public void submit(By locator) {
        driver.findElement(locator).submit();
    }

    /**
     * 模拟用户输入文本 1.清除文本 2.输入文本 3.输入回车键用于去掉控件焦点
     */
    public void type(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text + "\n");
    }
}
