package study.ywork.selenium.api.gesture;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DragAndDropTest {
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
    void testDragAndDrop() {
        driver.get("http://localhost:8080/drag-and-drop.html");
        Actions actions = new Actions(driver);

        WebElement draggable = driver.findElement(By.id("draggable"));
        int offset = 100;
        Point initLocation = draggable.getLocation();
        actions.dragAndDropBy(draggable, offset, 0).dragAndDropBy(draggable, 0, offset)
                .dragAndDropBy(draggable, -offset, 0).dragAndDropBy(draggable, 0, -offset).build().perform();
        assertThat(initLocation).isEqualTo(draggable.getLocation());

        WebElement target = driver.findElement(By.id("target"));
        actions.dragAndDrop(draggable, target).build().perform();
        assertThat(target.getLocation()).isEqualTo(draggable.getLocation());
    }
}
