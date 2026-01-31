package study.ywork.selenium.rest;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.Test;
import io.restassured.RestAssured;

public class RestTest {
    @Test
    void testRest() {
        HttpBinGet get = RestAssured.get("https://httpbin.org/get").then().assertThat().statusCode(200).extract()
                .as(HttpBinGet.class);
        assertThat(get.getHeaders()).containsKey("Accept-Encoding");
        assertThat(get.getOrigin()).isNotBlank();
    }
}
