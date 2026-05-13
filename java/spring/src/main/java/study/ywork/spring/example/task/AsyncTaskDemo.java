package study.ywork.spring.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class AsyncTaskDemo {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.runTasks();
        context.close();
    }

    @Configuration
    public static class MyConfig {
        @Bean
        MyBean myBean() {
            return new MyBean();
        }

        @Bean
        AsyncTaskExecutor taskExecutor() {
            SimpleAsyncTaskExecutor t = new SimpleAsyncTaskExecutor();
            t.setConcurrencyLimit(100);
            return t;
        }
    }

    private static class MyBean {
        @Autowired
        private AsyncTaskExecutor executor;

        public void runTasks() throws Exception {
            List<Future<?>> futureList = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                Future<?> future = executor.submit(getTask(i));
                futureList.add(future);
            }

            for (Future<?> future : futureList) {
                System.out.println(future.get());
            }
        }

        private Callable<String> getTask(int i) {
            return () -> {
                System.out.printf("任务 %d. 线程: %s%n", i, Thread.currentThread().getName());
                return String.format("任务完成 %d", i);
            };
        }
    }
}
