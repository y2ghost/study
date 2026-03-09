package study.ywork.users.redisson.locks;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class LockExamples {
    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RLock lock = redisson.getLock("lock");
        lock.lock(2, TimeUnit.SECONDS);

        Thread t = new Thread(() -> {
            RLock lock1 = redisson.getLock("lock");
            lock1.lock();
            lock1.unlock();
        });

        t.start();
        lock.unlock();
        t.join();
        redisson.shutdown();
    }
}
