package study.ywork.spring.example.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import java.time.LocalTime;

public class CronTriggerDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("任务开始执行: " + LocalTime.now());
        ThreadPoolTaskScheduler s = new ThreadPoolTaskScheduler();
        s.setPoolSize(5);
        s.initialize();

        for (int i = 0; i < 2; i++) {
            s.schedule(getTask(), new CronTrigger("* * * * * ?"));
        }

        s.getScheduledThreadPoolExecutor().shutdown();
    }

    public static Runnable getTask() {
        return () -> System.out.printf("任务: %s, 事件: %s:%n", Thread.currentThread().getName(), LocalTime.now());
    }
}