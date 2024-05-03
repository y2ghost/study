package study.ywork.spring.example.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

public class AsyncBasicDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("从线程调用异步方法: %s%n", Thread.currentThread().getName());
        bean.runTask();
        context.close();
    }

    @Configuration
    static class MyConfig {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }
    }

    static class MyBean {
        @Async
        public void runTask() {
            System.out.printf("任务运行线程: %s%n", Thread.currentThread().getName());
        }
    }
}