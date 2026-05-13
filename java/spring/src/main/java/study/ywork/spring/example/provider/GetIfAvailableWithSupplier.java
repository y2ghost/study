package study.ywork.spring.example.provider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.provider.SecondBean;

@Configuration
public class GetIfAvailableWithSupplier {
    // 如果去掉了@Bean注解，则返回'默认参数'构造对象
    @Bean
    SecondBean getSecondBean() {
        return new SecondBean("测试参数");
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            GetIfAvailableWithSupplier.class);
        ObjectProvider<SecondBean> beanProvider = context.getBeanProvider(SecondBean.class);
        SecondBean exampleBean = beanProvider.getIfAvailable(() -> new SecondBean("默认参数"));
        exampleBean.doSomething();
        context.close();
    }
}
