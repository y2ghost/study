package study.ywork.spring.example.task;

import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTaskSchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentTaskScheduler s = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(5));
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            s.schedule(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.printf("任务: %d. 线程: %s%n", finalI, Thread.currentThread().getName());
            }, new PeriodicTrigger(1000));
        }

        Thread.sleep(3000);
        System.out.println("--- 关闭 ----");
        ((ExecutorService) s.getConcurrentExecutor()).shutdown();
    }
}
