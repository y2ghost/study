package study.ywork.spring.example.env;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:app.properties")
public class BeanValueDemo {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    /* 必须是静态的方法，必须保证提前加载 */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanValueDemo.class);
        context.getBean(MyBean.class).showProp();
        context.close();
    }

    static class MyBean {
        @Value("${name:defaultName}")
        private String str;

        public void showProp() {
            System.out.println(str);
        }
    }
}
