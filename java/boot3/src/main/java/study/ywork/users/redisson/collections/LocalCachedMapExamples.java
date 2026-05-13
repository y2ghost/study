package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.LocalCachedMapOptions;
import org.redisson.api.options.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.options.LocalCachedMapOptions.ExpirationEventPolicy;
import org.redisson.api.options.LocalCachedMapOptions.SyncStrategy;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class LocalCachedMapExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        final LocalCachedMapOptions<String, Integer> options = LocalCachedMapOptions.<String, Integer>name("cachedMap")
                .cacheSize(10000)
                .maxIdle(Duration.ofSeconds(60))
                .timeToLive(Duration.ofSeconds(60))
                .evictionPolicy(EvictionPolicy.LFU)
                .syncStrategy(SyncStrategy.UPDATE)
                .expirationEventPolicy(ExpirationEventPolicy.SUBSCRIBE_WITH_KEYSPACE_CHANNEL);

        RLocalCachedMap<String, Integer> cachedMap = redisson.getLocalCachedMap(options);
        cachedMap.put("a", 1);
        cachedMap.put("b", 2);
        cachedMap.put("c", 3);

        boolean done = cachedMap.containsKey("a");
        System.out.println("containsKey " + done);

        Integer value = cachedMap.get("c");
        System.out.println(value);
        System.out.println("valueSize " + cachedMap.valueSize("c"));

        Set<String> keys = new HashSet<>();
        keys.add("a");
        keys.add("b");
        keys.add("c");
        cachedMap.getAll(keys);

        cachedMap.readAllKeySet();
        cachedMap.readAllValues();
        cachedMap.readAllEntrySet();

        cachedMap.fastPut("a", 100);
        cachedMap.fastPutIfAbsent("d", 33);
        cachedMap.fastRemove("b");

        redisson.shutdown();
    }
}
