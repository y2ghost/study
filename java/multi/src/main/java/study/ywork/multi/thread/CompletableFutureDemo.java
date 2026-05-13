package study.ywork.multi.thread;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class CompletableFutureDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletableFuture.runAsync(() -> performTask("first stage"), executor)
            .thenRun(() -> performTask("second stage"))
            .thenRunAsync(() -> performTask("third stage"), executor)
            .join();// 等待任务完成
        System.out.println("main exiting");
        executor.shutdown();

        // SupplyAsync示例
        CompletableFuture.supplyAsync(() -> ThreadLocalRandom.current().nextInt(1, 10))
            .thenApply(Math::sqrt)
            .thenAccept(System.out::println)
            .join();
    }

    private static void performTask(String stage) {
        System.out.println("---------");
        System.out.printf("stage: %s, time before task: %s, thread: %s%n", stage, LocalTime.now(),
            Thread.currentThread().getName());
        try {
            // 模拟耗时任务
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

        System.out.printf("stage: %s, time after task: %s, thread: %s%n", stage, LocalTime.now(),
            Thread.currentThread().getName());
    }
}
