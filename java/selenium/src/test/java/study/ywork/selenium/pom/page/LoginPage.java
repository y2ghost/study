package study.ywork.selenium.pom.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    By usernameInput = By.id("username");
    By passwordInput = By.id("password");
    By submitButton = By.cssSelector("button");
    By successBox = By.id("success");

    public LoginPage(WebDriver driver, int timeoutSec) {
        this(driver);
        setTimeoutSec(timeoutSec);
    }

    public LoginPage(WebDriver driver) {
        super(driver);
        visit("http://localhost:8080/login-form.html");
    }

    public void with(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(submitButton);
    }

    public boolean successBoxPresent() {
        return isDisplayed(successBox);
    }
}
