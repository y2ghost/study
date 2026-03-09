package study.ywork.users.redisson.locks;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;

public class FairLockExamples {
    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        RLock lock = redisson.getFairLock("test");

        int size = 10;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Thread t = new Thread(() -> {
                lock.lock();
                lock.unlock();
            });

            threads.add(t);
        }

        for (Thread thread : threads) {
            thread.start();
            thread.join(5);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        redisson.shutdown();
    }
}
