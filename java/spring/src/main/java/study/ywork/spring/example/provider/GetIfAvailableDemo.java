package study.ywork.spring.example.provider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.provider.FirstBean;

@Configuration
public class GetIfAvailableDemo {
    @Bean
    FirstBean getFirstBean() {
        return new FirstBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GetIfAvailableDemo.class);
        ObjectProvider<FirstBean> beanProvider = context.getBeanProvider(FirstBean.class);
        FirstBean firstBean = beanProvider.getIfAvailable();
        System.out.println("firstBean: " + firstBean);

        if (firstBean != null) {
            firstBean.doSomething();
        }

        context.close();
    }
}
