package study.ywork.selenium.api.locator;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DatePickerTest {
    private static final Logger log = getLogger(lookup().lookupClass());
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
    void testDatePicker() {
        driver.get("http://localhost:8080/web-form.html");
        // 获取系统当前时间
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentDay = today.getDayOfMonth();

        // 点击日期选择器打开日历
        WebElement datePicker = driver.findElement(By.name("my-date"));
        datePicker.click();

        // 点击当前月份
        WebElement monthElement = driver
                .findElement(By.xpath(String.format("//th[contains(text(),'%d')]", currentYear)));
        monthElement.click();

        // 使用相对定位器点击左侧箭头
        WebElement arrowLeft = driver.findElement(RelativeLocator.with(By.tagName("th")).toRightOf(monthElement));
        arrowLeft.click();

        // 点击月份
        WebElement monthPastYear = driver
                .findElement(RelativeLocator.with(By.cssSelector("span[class$=focused]")).below(arrowLeft));
        monthPastYear.click();

        // 点击当前日
        WebElement dayElement = driver
                .findElement(By.xpath(String.format("//td[@class='day' and contains(text(),'%d')]", currentDay)));
        dayElement.click();

        // 获取日期值
        String oneYearBack = datePicker.getAttribute("value");
        log.debug("最终选择的日期值: {}", oneYearBack);

        LocalDate previousYear = today.minusYears(1);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String expectedDate = previousYear.format(dateFormat);
        log.debug("期望的日期值: {}", expectedDate);
        assertThat(oneYearBack).isEqualTo(expectedDate);
    }
}
