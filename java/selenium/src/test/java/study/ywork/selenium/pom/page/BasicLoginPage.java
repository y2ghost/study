package study.ywork.selenium.pom.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BasicLoginPage {
    private WebDriver driver;
    By usernameInput = By.id("username");
    By passwordInput = By.id("password");
    By submitButton = By.cssSelector("button");
    By successBox = By.id("success");

    public BasicLoginPage(WebDriver driver) {
        this.driver = driver;
        driver.get("http://localhost:8080/login-form.html");
    }

    public void with(String username, String password) {
        driver.findElement(usernameInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(submitButton).click();
    }

    public boolean successBoxPresent() {
        return driver.findElement(successBox).isDisplayed();
    }
}
