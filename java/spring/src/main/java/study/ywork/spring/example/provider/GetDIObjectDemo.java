package study.ywork.spring.example.provider;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.provider.FirstBean;

@Configuration
public class GetDIObjectDemo {
    @Bean
    ClientBean clientBean() {
        return new ClientBean();
    }

    @Bean
    FirstBean getFirstBean() {
        return new FirstBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GetDIObjectDemo.class);
        context.close();
    }

    private static class ClientBean {
        @Autowired
        ObjectProvider<FirstBean> objectProvider;

        @PostConstruct
        void postConstruct() {
            if (objectProvider != null) {
                FirstBean firstBean = objectProvider.getObject();
                firstBean.doSomething();
            }
        }
    }
}
