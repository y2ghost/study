package study.ywork.users.redisson.locks;

import org.redisson.Redisson;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class PermitExpireSemaphoreExamples {

    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RPermitExpirableSemaphore s = redisson.getPermitExpirableSemaphore("test");
        s.trySetPermits(1);
        String permitId = s.tryAcquire(100, 2, TimeUnit.SECONDS);

        Thread t = new Thread(() -> {
            RPermitExpirableSemaphore s1 = redisson.getPermitExpirableSemaphore("test");
            try {
                String permitId1 = s1.acquire();
                s1.release(permitId1);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        t.start();
        t.join();

        s.tryRelease(permitId);
        redisson.shutdown();
    }
}
