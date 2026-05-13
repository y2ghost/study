package study.ywork.users.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试方法
 * curl -L -X GET -H "X-API-KEY: tester" http://localhost:8080/home
 */
@RestController
public class HomeController {
    @GetMapping("/home")
    public String homeEndpoint() {
        return "welcome to home!";
    }
}
