package study.ywork.restassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

// 认证测试示例
public class RestAuthTest {
    @Test
    public void basicAuthTest() {
        RequestSpecification httpRequest = RestAssured.given().auth().basic("postman", "password");
        Response res = httpRequest.get("https://postman-echo.com/basic-auth");
        printAuthResult(res);

    }

    @Test
    public void challengeAuthTest() {
        RequestSpecification httpRequest = RestAssured.given().auth().preemptive().basic("postman", "password");
        Response res = httpRequest.get("https://postman-echo.com/basic-auth");
        printAuthResult(res);
    }

    private void printAuthResult(Response res) {
        ResponseBody<?> body = res.body();
        String rbdy = body.asString();
        System.out.println("认证结果 - " + rbdy);
    }
}
