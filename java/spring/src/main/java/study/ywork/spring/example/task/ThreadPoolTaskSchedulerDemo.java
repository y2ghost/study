package study.ywork.spring.example.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import java.time.LocalTime;

public class ThreadPoolTaskSchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolTaskScheduler s = new ThreadPoolTaskScheduler();
        s.setPoolSize(5);
        s.initialize();

        for (int i = 0; i < 2; i++) {
            s.scheduleAtFixedRate(
                () -> System.out.printf("任务: %s, 时间: %s:%n", Thread.currentThread().getName(), LocalTime.now()), 1000);
        }

        Thread.sleep(10000);
        System.out.println("10秒后关闭");
        s.getScheduledThreadPoolExecutor().shutdownNow();
    }
}
