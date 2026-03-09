package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;

public class BitSetExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        RBitSet bs = redisson.getBitSet("testBitset");
        bs.set(0, 5);
        bs.clear(0, 1);
        bs.length();

        bs.clear();
        bs.set(28);
        bs.get(28);

        bs.not();

        bs.cardinality();

        bs.set(3, true);
        bs.set(41, false);

        RBitSet bs1 = redisson.getBitSet("testBitset1");
        bs1.set(3, 5);

        RBitSet bs2 = redisson.getBitSet("testBitset2");
        bs2.set(4);
        bs2.set(10);
        bs1.and(bs2.getName());
        bs1.or(bs2.getName());
        bs1.xor(bs2.getName());

        redisson.shutdown();
    }
}
