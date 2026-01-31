package study.ywork.users.redisson.locks;

import org.redisson.Redisson;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchExamples {

    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        final RCountDownLatch latch = redisson.getCountDownLatch("latch1");
        latch.trySetCount(1);

        executor.execute(latch::countDown);

        executor.execute(() -> {
            try {
                latch.await(550, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        redisson.shutdown();
    }
}
