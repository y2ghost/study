package study.ywork.restassured;

import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class RestPostTest {
    public static class SucessVo {
        public String code;
        public String message;
    }

    public static class UserVo {
        public String userName;
        public String password;
    }

    @Test
    void newBookTest() throws JsonProcessingException {
        RestAssured.baseURI = "https://localhost:8080/";
        RequestSpecification request = RestAssured.given();
        UserVo user = new UserVo();
        user.userName = "test1";
        user.password = "test@123";
        JsonMapper om = JsonMapper.builder().build();
        request.body(om.writeValueAsString(user));
        Response response = request.post("/users");
        ResponseBody<?> body = response.getBody();
        System.out.println(response.getStatusLine());
        System.out.println(body.as(SucessVo.class));
    }
}
