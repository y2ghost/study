package study.ywork.spring.example.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WithoutCglibDemo {
    private int counter;

    public String something() {
        return String.valueOf(++counter);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WithoutCglibDemo.class);
        WithoutCglibDemo bean = context.getBean(WithoutCglibDemo.class);
        // 输出结果不同
        System.out.println(bean.something());
        System.out.println(bean.something());
        context.close();
    }
}
