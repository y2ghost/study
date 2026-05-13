package study.ywork.users.redisson.locks;

import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;

public class SemaphoreExamples {
    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RSemaphore s = redisson.getSemaphore("test");
        s.trySetPermits(5);
        s.acquire(3);

        Thread t = new Thread(() -> {
            RSemaphore s1 = redisson.getSemaphore("test");
            s1.release();
            s1.release();
        });

        t.start();

        s.acquire(4);

        redisson.shutdown();
    }
}
