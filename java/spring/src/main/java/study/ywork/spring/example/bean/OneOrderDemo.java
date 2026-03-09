package study.ywork.spring.example.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.bean.qualifier.OrderServiceClient;
import study.ywork.spring.bean.qualifier.service.OneOrderServiceImpl;
import study.ywork.spring.bean.qualifier.service.OrderService;
import study.ywork.spring.bean.qualifier.service.TwoOrderServiceImpl;

@Configuration
public class OneOrderDemo {
    @Bean(name = "orderServiceOne")
    public OrderService orderServiceByProvider1() {
        return new OneOrderServiceImpl();
    }

    @Bean(name = "orderServiceTwo")
    public OrderService orderServiceByProvider2() {
        return new TwoOrderServiceImpl();
    }

    @Bean
    public OrderServiceClient createClient() {
        return new OrderServiceClient();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OneOrderDemo.class);
        OrderServiceClient bean = context.getBean(OrderServiceClient.class);
        bean.showPendingOrderDetails();
        context.close();
    }
}
