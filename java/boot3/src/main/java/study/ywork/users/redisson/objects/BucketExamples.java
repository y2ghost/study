package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.time.Duration;

public class BucketExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RBucket<String> bucket = redisson.getBucket("test");
        bucket.set("123");
        boolean isUpdated = bucket.compareAndSet("123", "4934");
        System.out.println("isUpdated " + isUpdated);

        String prevObject = bucket.getAndSet("321");
        System.out.println("prevObject " + prevObject);

        boolean isSet = bucket.setIfAbsent("901");
        System.out.println("isSet " + isSet);

        long objectSize = bucket.size();
        System.out.println("objectSize " + objectSize);

        // 设置超时
        bucket.set("value", Duration.ofSeconds(10));
        boolean isNewSet = bucket.setIfAbsent("nextValue", Duration.ofSeconds(10));
        System.out.println("isNewSet " + isNewSet);

        redisson.shutdown();
    }
}
