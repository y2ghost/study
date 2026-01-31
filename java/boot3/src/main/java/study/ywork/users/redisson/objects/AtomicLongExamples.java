package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

public class AtomicLongExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RAtomicLong atomicLong = redisson.getAtomicLong("myLong");
        atomicLong.getAndDecrement();
        atomicLong.getAndIncrement();

        atomicLong.addAndGet(10L);
        atomicLong.compareAndSet(29, 412);

        atomicLong.decrementAndGet();
        atomicLong.incrementAndGet();

        atomicLong.getAndAdd(302);
        atomicLong.getAndDecrement();
        atomicLong.getAndIncrement();

        redisson.shutdown();
    }
}
