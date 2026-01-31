package study.ywork.spring.example.provider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.provider.MsgBean;

@Configuration
public class StreamDemo {
    @Bean
    MsgBean msgBean() {
        return new MsgBean("测试消息1");
    }

    @Bean
    MsgBean msgBean2() {
        return new MsgBean("测试消息2");
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(StreamDemo.class);
        ObjectProvider<MsgBean> beanProvider = context.getBeanProvider(MsgBean.class);
        beanProvider.iterator().forEachRemaining(System.out::println);
        beanProvider.forEach(System.out::println);
        context.close();
    }
}
