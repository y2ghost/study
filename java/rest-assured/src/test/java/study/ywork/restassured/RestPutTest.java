package study.ywork.restassured;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RestPutTest {
    String userId = "toolsqa_test";
    String baseUrl = "https://localhost:8080";
    String token = "XXX";
    String isbn = "YYYY";

    @Test
    public void putTest() {
        RestAssured.baseURI = baseUrl;
        RequestSpecification httpRequest = RestAssured.given().header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
        Response res = httpRequest.body("{ \"isbn\": \"" + isbn + "\", \"userId\": \"" + userId + "\"}")
                .put("/books/YYYY");
        System.out.println("响应代码 - " + res.getStatusCode());
        Assert.assertEquals(res.getStatusCode(), 200);
    }
}
