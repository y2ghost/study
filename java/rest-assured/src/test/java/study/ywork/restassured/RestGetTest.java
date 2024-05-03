package study.ywork.restassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestGetTest {
    @Test
    void getTest() {
        // REST API的基本URL地址
        RestAssured.baseURI = "https://httpbin.org/get";
        RequestSpecification httpRequest = RestAssured.given();
        // 使用GET请求方法
        Response response = httpRequest.request(Method.GET, "");
        System.out.println("响应状态 => " + response.getStatusLine());
        System.out.println("响应数据 => " + response.prettyPrint());
    }
}
