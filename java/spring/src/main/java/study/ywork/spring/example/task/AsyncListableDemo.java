package study.ywork.spring.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.util.concurrent.Callable;

public class AsyncListableDemo {
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
        AsyncListenableTaskExecutor taskExecutor() {
            SimpleAsyncTaskExecutor t = new SimpleAsyncTaskExecutor();
            t.setConcurrencyLimit(100);
            return t;
        }

        @Bean
        ListenableFutureCallback<String> taskCallback() {
            return new MyListenableFutureCallback();
        }
    }

    private static class MyBean {
        @Autowired
        private AsyncListenableTaskExecutor executor;
        @Autowired
        private ListenableFutureCallback<String> threadListenableCallback;

        public void runTasks() {

            for (int i = 0; i < 10; i++) {
                ListenableFuture<String> f = executor.submitListenable(getTask(i));
                f.addCallback(threadListenableCallback);
            }
        }

        private Callable<String> getTask(int i) {
            return () -> {
                System.out.printf("任务 %d. 线程: %s%n", i, Thread.currentThread().getName());
                return String.format("任务完成 %d", i);
            };
        }
    }

    private static class MyListenableFutureCallback implements ListenableFutureCallback<String> {
        @Override
        public void onFailure(Throwable ex) {
            System.out.println("失败消息: " + ex.getMessage());
            ex.printStackTrace();
        }

        @Override
        public void onSuccess(String result) {
            System.out.println("成功结果: " + result);
        }
    }
}
