package study.ywork.restassured;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

// 验证HTTP JSON BODY数据
public class RestBodyTest {
    @Test
    void bodyTest() {
        RestAssured.baseURI = "https://httpbin.org/";
        RequestSpecification httpRequest = RestAssured.given();
        // 此处参数无用，仅仅用于演示用法
        Response response = httpRequest.queryParam("name", "useless").get("/json");
        JsonPath jsonPathEvaluator = response.jsonPath();
        String title = jsonPathEvaluator.get("slideshow.title");
        System.out.println("title: " + title);
        assertThat(title).isEqualTo("Sample Slide Show");
    }
}
