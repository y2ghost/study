package study.ywork.spring.hello;

import org.springframework.beans.factory.annotation.Autowired;
import study.ywork.spring.hello.service.HelloService;

public class HelloServiceClient {
    @Autowired
    private HelloService helloService;

    public void helloName() {
        helloService.sayHi("YY");
    }
}
