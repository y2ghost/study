package study.ywork.spring.example.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class PeriodicTriggerDemo {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolTaskScheduler s = new ThreadPoolTaskScheduler();
        s.setPoolSize(5);
        s.initialize();

        for (int i = 0; i < 2; i++) {
            s.schedule(getTask(), new PeriodicTrigger(5, TimeUnit.SECONDS));
        }

        Thread.sleep(10000);
        System.out.println("10秒后关闭");
        s.getScheduledThreadPoolExecutor().shutdownNow();
    }

    public static Runnable getTask() {
        return () -> System.out.printf("任务: %s, 时间: %s:%n", Thread.currentThread().getName(), LocalTime.now());
    }
}