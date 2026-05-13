package study.ywork.spring.example.provider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.provider.FirstBean;

@Configuration
public class GetIfUniqueDemo {
    @Bean
    FirstBean getFirstBean() {
        return new FirstBean();
    }

    // 如果该函数添加了@Bean的注解，则不保证唯一性了
    FirstBean getFirstBean2() {
        return new FirstBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GetIfUniqueDemo.class);
        ObjectProvider<FirstBean> beanProvider = context.getBeanProvider(FirstBean.class);
        FirstBean firstBean = beanProvider.getIfUnique();
        System.out.println("firstBean: " + firstBean);

        if (firstBean != null) {
            firstBean.doSomething();
        }

        context.close();
    }
}