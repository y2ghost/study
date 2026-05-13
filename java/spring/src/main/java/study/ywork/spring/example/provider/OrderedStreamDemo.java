package study.ywork.spring.example.provider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.provider.OrderMsgBean;

@Configuration
public class OrderedStreamDemo {
    @Bean
    OrderMsgBean msgBean() {
        return new OrderMsgBean("测试消息1", 2);
    }

    @Bean
    OrderMsgBean msgBean2() {
        return new OrderMsgBean("测试消息2", 1);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OrderedStreamDemo.class);
        ObjectProvider<OrderMsgBean> beanProvider = context.getBeanProvider(OrderMsgBean.class);
        System.out.println("-- 默认顺序 --");
        beanProvider.stream().forEach(System.out::println);
        System.out.println("-- 排序顺序 --");
        beanProvider.orderedStream().forEach(System.out::println);
        context.close();
    }
}