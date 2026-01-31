package study.ywork.selenium.pom.test;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import study.ywork.selenium.pom.page.ExtendedLoginPage;

public class ExtendedLoginTest {
    private ExtendedLoginPage login;

    @BeforeMethod
    void setup() {
        login = new ExtendedLoginPage("chrome");
    }

    @AfterMethod
    void teardown() {
        login.quit();
    }

    @Test
    void testLoginSuccess() {
        login.with("user", "user");
        assertThat(login.successBoxPresent()).isTrue();
    }

    @Test
    void testLoginFailure() {
        login.with("bad-user", "bad-password");
        assertThat(login.successBoxPresent()).isFalse();
    }
}
