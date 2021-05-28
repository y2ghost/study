package study.ywork.spring.example.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * 记住AsyncConfigurer配置类初始化很早，在application context之前，所以凡是依赖它的Bean建议懒加载
 * 该例子属于简单示例，无需关注运行中的报警信息
 */
public class AsyncConfigurerDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("调用MyBean#runTask() 线程: %s%n", Thread.currentThread().getName());
        bean.runTask();
        ConcurrentTaskExecutor exec = (ConcurrentTaskExecutor) context.getBean("taskExecutor");
        ExecutorService es = (ExecutorService) exec.getConcurrentExecutor();
        es.shutdown();
        context.close();
    }

    @EnableAsync
    @Configuration
    static class MyConfig implements AsyncConfigurer {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }

        @Bean(name = "taskExecutor")
        @Override
        public Executor getAsyncExecutor() {
            return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
        }

        @Bean
        @Qualifier("myExecutor")
        public Executor getMyAsyncExecutor() {
            return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
        }

        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return (throwable, method, objects) -> System.out.println("-- 异常处理器 -- " + throwable);
        }
    }

    static class MyBean {
        // 示例指定TaskExecutor
        @Async("myExecutor")
        public void runTask() {
            System.out.printf("任务运行线程: %s%n", Thread.currentThread().getName());
            throw new RuntimeException("测试异常");
        }
    }
}