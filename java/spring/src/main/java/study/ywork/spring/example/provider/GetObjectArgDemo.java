package study.ywork.spring.example.provider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import study.ywork.spring.provider.SecondBean;

@Configuration
public class GetObjectArgDemo {
    @Bean
    @Lazy
    SecondBean getSecondBean(String arg) {
        return new SecondBean(arg);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GetObjectArgDemo.class);
        ObjectProvider<SecondBean> beanProvider = context.getBeanProvider(SecondBean.class);
        SecondBean secondBean = beanProvider.getObject("测试参数");
        System.out.println("SecondBean: " + secondBean);
        secondBean.doSomething();
        context.close();
    }
}