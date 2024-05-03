package study.ywork.restassured;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestHeaderTest {
    private static final String CONTENT_TYPE = "Content-Type";

    @Test
    void headerTest() {
        RestAssured.baseURI = "https://httpbin.org/get";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get();
        Headers headers = response.headers();
        assertThat(headers).hasSizeGreaterThan(1);
        headers.forEach(header -> {
            System.out.println(header.getName() + ": " + header.getValue());

        });
        System.out.println(CONTENT_TYPE + ": " + response.header(CONTENT_TYPE));
    }
}
