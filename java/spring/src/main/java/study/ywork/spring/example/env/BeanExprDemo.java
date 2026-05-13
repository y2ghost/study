package study.ywork.spring.example.env;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanExprDemo {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanExprDemo.class);
        context.getBean(MyBean.class).showProp();
        context.close();
    }

    public static class MyBean {
        @Value("#{systemProperties['user.home']}")
        private String userHome;

        public void showProp() {
            System.out.println(userHome);
        }
    }
}
