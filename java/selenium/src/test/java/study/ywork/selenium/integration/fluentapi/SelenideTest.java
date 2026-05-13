package study.ywork.selenium.integration.fluentapi;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class SelenideTest {
    @Test
    void testSelenide() {
        open("http://localhost:8080/login-form.html");
        $(By.id("username")).val("user");
        $(By.id("password")).val("user");
        $("button").pressEnter();
        $(By.id("success")).shouldBe(visible).shouldHave(text("登陆成功"));
    }
}
