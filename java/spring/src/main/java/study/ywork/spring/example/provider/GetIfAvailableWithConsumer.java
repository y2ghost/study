package study.ywork.spring.example.provider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.provider.SecondBean;

@Configuration
public class GetIfAvailableWithConsumer {
    // 如果去掉了@Bean注解，则什么都不打印
    @Bean
    SecondBean getSecondBean() {
        return new SecondBean("测试参数");
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            GetIfAvailableWithConsumer.class);
        ObjectProvider<SecondBean> beanProvider = context.getBeanProvider(SecondBean.class);
        beanProvider.ifAvailable(SecondBean::doSomething);
        context.close();
    }
}
