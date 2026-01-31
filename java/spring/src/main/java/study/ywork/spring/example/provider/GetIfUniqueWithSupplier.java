package study.ywork.spring.example.provider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.provider.SecondBean;

@Configuration
public class GetIfUniqueWithSupplier {
    @Bean
    SecondBean getSecondBean() {
        return new SecondBean("测试参数1");
    }

    // 如果加上了@Bean注解，则返回'默认参数'构造对象
    // @Bean
    SecondBean getSecondBean2() {
        return new SecondBean("测试参数2");
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            GetIfUniqueWithSupplier.class);
        ObjectProvider<SecondBean> beanProvider = context.getBeanProvider(SecondBean.class);
        SecondBean exampleBean = beanProvider.getIfUnique(() -> new SecondBean("默认参数"));
        exampleBean.doSomething();
        context.close();
    }
}
