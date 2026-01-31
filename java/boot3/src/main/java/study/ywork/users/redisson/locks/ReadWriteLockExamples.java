package study.ywork.users.redisson.locks;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

public class ReadWriteLockExamples {
    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        final RReadWriteLock lock = redisson.getReadWriteLock("lock");

        boolean done = lock.writeLock().tryLock();
        System.out.println("tryLock " + done);

        Thread t = new Thread(() -> {
            RLock r = lock.readLock();
            r.lock();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
            r.unlock();
        });

        t.start();
        t.join();

        lock.writeLock().unlock();

        t.join();
        redisson.shutdown();
    }
}
