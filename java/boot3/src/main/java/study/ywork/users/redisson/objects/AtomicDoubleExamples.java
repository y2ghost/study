package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RedissonClient;

public class AtomicDoubleExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        RAtomicDouble atomicDouble = redisson.getAtomicDouble("myDouble");
        atomicDouble.getAndDecrement();
        atomicDouble.getAndIncrement();

        atomicDouble.addAndGet(10.323);
        atomicDouble.compareAndSet(29.4, 412.91);

        atomicDouble.decrementAndGet();
        atomicDouble.incrementAndGet();

        atomicDouble.getAndAdd(302.00);
        atomicDouble.getAndDecrement();
        atomicDouble.getAndIncrement();

        redisson.shutdown();
    }
}
