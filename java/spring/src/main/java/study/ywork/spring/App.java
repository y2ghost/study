package study.ywork.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.hello.HelloServiceClient;
import study.ywork.spring.hello.service.HelloService;
import study.ywork.spring.hello.service.HelloServiceImpl;

@Configuration
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        HelloServiceClient bean = context.getBean(HelloServiceClient.class);
        bean.helloName();
        context.close();
    }

    public String getGreeting() {
        return "Hello YY.";
    }

    @Bean
    public HelloService createHelloService() {
        return new HelloServiceImpl();
    }

    @Bean
    public HelloServiceClient createClient() {
        return new HelloServiceClient();
    }
}
