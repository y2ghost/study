package study.ywork.selenium.browser.basic;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BrokenLinks {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cnblogs.com/");
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("网页链接数 " + links.size());

        for (int i = 0; i < links.size(); i++) {
            WebElement E1 = links.get(i);
            String url = E1.getAttribute("href");
            verifyLinks(url);
        }

        List<WebElement> images = driver.findElements(By.tagName("img"));
        System.out.println("图片链接数 " + images.size());

        for (int index = 0; index < images.size(); index++) {
            WebElement image = images.get(index);
            String imageURL = image.getAttribute("src");
            System.out.println("URL of Image " + (index + 1) + " is: " + imageURL);
            verifyLinks(imageURL);

            try {
                boolean imageDisplayed = (Boolean) ((JavascriptExecutor) driver).executeScript(
                        "return (typeof arguments[0].naturalWidth !=\"undefined\" && arguments[0].naturalWidth > 0);",
                        image);
                if (imageDisplayed) {
                    System.out.println("显示正常");
                } else {
                    System.out.println("显示异常");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        driver.quit();
    }

    public static void verifyLinks(String linkUrl) {
        try {
            URL url = new URL(linkUrl);
            HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();
            httpURLConnect.setConnectTimeout(5000);
            httpURLConnect.connect();

            if (httpURLConnect.getResponseCode() >= 400) {
                System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage() + " 链接已坏");
            } else {
                System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
