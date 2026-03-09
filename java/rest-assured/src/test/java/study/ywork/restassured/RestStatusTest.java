package study.ywork.restassured;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

// 
public class RestStatusTest {
    @Test
    void statusTest() {
        RestAssured.baseURI = "https://httpbin.org/get";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get();
        int statusCode = response.getStatusCode();
        // 两种形式的断言判断均可以，第一种属于流行的断言库，第二种testng内置的方法
        assertThat(statusCode).isEqualTo(200);
        Assert.assertEquals(statusCode, 200);
        System.out.println("响应状态 => " + response.getStatusLine());
    }
}
