package study.ywork.spring.example.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

public class AsyncOnClassDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("从线程调用异步方法: %s%n", Thread.currentThread().getName());
        bean.runTask1();
        bean.runTask2();
        context.close();
    }

    @Configuration
    static class MyConfig {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }
    }

    /*
     * 整个类的方法都是异步处理
     */
    @Async
    static class MyBean {
        public void runTask1() {
            System.out.printf("runTask1 线程: %s%n", Thread.currentThread().getName());
        }

        public void runTask2() {
            System.out.printf("runTask2 线程: %s%n", Thread.currentThread().getName());
        }
    }
}
