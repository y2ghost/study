package study.ywork.restassured;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class RestDeleteTest {
    private String userId = "toolsqa_test";
    private String baseUrl = "https://localhost:8080";
    private String token = "XXX";
    private String isbn = "YYYY";

    @BeforeTest
    @AfterTest
    public void getTest() {
        RestAssured.baseURI = baseUrl;
        RequestSpecification httpRequest = RestAssured.given().header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
        Response res = httpRequest.get("/users/" + userId);
        ResponseBody<?> body = res.body();
        String rbdy = body.asString();
        System.out.println("响应数据 - " + rbdy);
    }

    @Test
    public void deleteTest() {
        RestAssured.baseURI = baseUrl;
        RequestSpecification httpRequest = RestAssured.given().header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
        Response res = httpRequest.body("{ \"isbn\": \"" + isbn + "\", \"userId\": \"" + userId + "\"}")
                .delete("/books");
        System.out.println("响应代码 - " + res.getStatusCode());
        Assert.assertEquals(res.getStatusCode(), 204);
    }
}
