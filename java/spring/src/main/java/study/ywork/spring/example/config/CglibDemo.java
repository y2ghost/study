package study.ywork.spring.example.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CglibDemo {
    private int counter;

    @Bean
    public String something() {
        return String.valueOf(++counter);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CglibDemo.class);
        CglibDemo bean = context.getBean(CglibDemo.class);
        // 输出结果都是相同的，因为CglibDemo类的实例被CGLIB代理了，生成的BEAN对象只有一个
        System.out.println(bean.something());
        System.out.println(bean.something());
        context.close();
    }
}
