package study.ywork.spring.example.env;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import java.util.Map;

public class SystemSourceDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ConfigurableEnvironment env = context.getEnvironment();
        printSources(env);
        System.out.println("---- 系统属性-----");
        printMap(env.getSystemProperties());
        System.out.println("---- 系统环境属性 -----");
        printMap(env.getSystemEnvironment());
        context.close();
    }

    private static void printSources(ConfigurableEnvironment env) {
        System.out.println("---- 属性源 ----");
        for (PropertySource<?> propertySource : env.getPropertySources()) {
            System.out.printf("名称 = %s, 源 = %s%n", propertySource.getName(), propertySource.getSource().getClass());
        }
    }

    private static void printMap(Map<?, ?> map) {
        map.entrySet().stream().forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));
    }
}