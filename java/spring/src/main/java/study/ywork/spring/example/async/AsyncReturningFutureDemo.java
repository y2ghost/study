package study.ywork.spring.example.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.CompletableFuture;

public class AsyncReturningFutureDemo {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("调用 MyBean#runTask() 线程: %s%n", Thread.currentThread().getName());
        CompletableFuture<String> r = bean.runTask();
        System.out.println("任务结果:" + r.get());
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
        public CompletableFuture<String> runTask() {
            System.out.printf("运行任务线程: %s%n", Thread.currentThread().getName());
            return new CompletableFuture<>() {
                @Override
                public String get() {
                    return "这是结果";
                }
            };
        }
    }
}
