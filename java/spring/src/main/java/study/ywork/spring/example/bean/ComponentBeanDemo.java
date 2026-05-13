package study.ywork.spring.example.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

/*
 * Component组件类里面定义BEAN的例子，不被CGLIB代理
 * 用于无法修改@Configuration类文件，想定义特定模块的BEAN等场景
 */
@Configuration
@ComponentScan(basePackageClasses = ComponentBeanDemo.class, useDefaultFilters = false, includeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ComponentBeanDemo.OneTestBean.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ComponentBeanDemo.TwoTestBean.class) })
public class ComponentBeanDemo {
    public static void main(String[] args) {
        System.out.println("demo1 test");
        demo1();
        System.out.println("demo2 test");
        demo2();
    }

    public static void demo1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ComponentBeanDemo.class);
        AnotherTestBean bean = context.getBean(AnotherTestBean.class);
        System.out.println(bean);

        // 多次访问
        bean = context.getBean(AnotherTestBean.class);
        System.out.println(bean);

        // CGLIB代理的类
        OneTestBean testBean = context.getBean(OneTestBean.class);
        System.out.println(testBean.anotherTestBean());
        context.close();
    }

    public static void demo2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ComponentBeanDemo.class);
        AnotherTestBean bean = context.getBean(AnotherTestBean.class);
        System.out.println(bean);

        // 多次访问
        bean = context.getBean(AnotherTestBean.class);
        System.out.println(bean);

        // CGLIB代理的类
        TwoTestBean testBean = context.getBean(TwoTestBean.class);
        System.out.println(testBean.anotherTestBean());
        context.close();
    }

    @Configuration
    public static class OneTestBean {
        @Bean
        public AnotherTestBean anotherTestBean() {
            return new AnotherTestBean();
        }
    }

    @Component
    public static class TwoTestBean {
        @Bean
        public AnotherTestBean anotherTestBean() {
            return new AnotherTestBean();
        }
    }

    public static class AnotherTestBean {
        AnotherTestBean() {
            // 不做事儿
        }
    }
}
