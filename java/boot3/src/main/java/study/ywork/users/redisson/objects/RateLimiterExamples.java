package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class RateLimiterExamples {
    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RRateLimiter limiter = redisson.getRateLimiter("myLimiter");
        // 允许1个/每2秒
        limiter.trySetRate(RateType.OVERALL, 1, Duration.ofSeconds(2));

        CountDownLatch latch = new CountDownLatch(2);
        limiter.acquire(1);
        latch.countDown();

        Thread t = new Thread(() -> {
            limiter.acquire(1);

            latch.countDown();
        });
        t.start();
        t.join();

        latch.await();

        redisson.shutdown();
    }
}
