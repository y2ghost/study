package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

public class BloomFilterExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("bloomFilter");
        bloomFilter.tryInit(100_000_000, 0.03);

        bloomFilter.add("a");
        bloomFilter.add("b");
        bloomFilter.add("c");
        bloomFilter.add("d");

        bloomFilter.getExpectedInsertions();
        bloomFilter.getFalseProbability();
        bloomFilter.getHashIterations();

        bloomFilter.contains("a");

        bloomFilter.count();

        redisson.shutdown();
    }
}
