package study.ywork.selenium.api.gesture;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MouseOverTest {
    private WebDriver driver;

    @BeforeMethod
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }

    @AfterMethod
    void teardown() {
        driver.quit();
    }

    @Test
    void testMouseOver() {
        driver.get("http://localhost:8080/mouse-over.html");
        Actions actions = new Actions(driver);
        List<String> imageList = Arrays.asList("compass", "calendar", "award", "landscape");

        for (String picName : imageList) {
            String xpath = String.format("//img[@src='img/%s.png']", picName);
            WebElement image = driver.findElement(By.xpath(xpath));
            actions.moveToElement(image).build().perform();
            WebElement caption = driver.findElement(RelativeLocator.with(By.tagName("div")).near(image));
            assertThat(caption.getText()).containsIgnoringCase(getImageName(picName));
        }
    }

    private String getImageName(String picName) {
        switch (picName) {
        case "compass":
            return "指南针";
        case "calendar":
            return "日历";
        case "award":
            return "奖品";
        case "landscape":
            return "景色";
        default:
            return "未知";
        }
    }
}
