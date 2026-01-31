package study.ywork.spring.example.task;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

public class StandAloneDemo {
    public static void main(String[] args) {
        TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
        theExecutor.execute(() -> {
            System.out.println("running task in thread: " + Thread.currentThread().getName());
        });
        System.out.println("in main thread: " + Thread.currentThread().getName());
    }
}
