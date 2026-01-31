package study.ywork.users.redisson.locks;

import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class MultiLockExamples {
    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient client = Redisson.create();
        RLock lock1 = client.getLock("lock1");
        RLock lock2 = client.getLock("lock2");
        RLock lock3 = client.getLock("lock3");

        Thread t = new Thread(() -> {
            RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
            lock.lock();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }

            lock.unlock();
        });
        t.start();
        t.join(1000);

        RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
        lock.lock();
        lock.unlock();

        client.shutdown();
    }
}
