package study.ywork.spring.example.env;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:app.properties")
public class PropertyBeanDemo {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PropertyBeanDemo.class);
        context.getBean(MyBean.class).showProp();
        context.close();
    }

    static class MyBean {
        @Autowired
        Environment env;

        public void showProp() {
            System.out.println(env.getProperty("name"));
        }
    }
}