package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RSet;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SetCacheExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        RSetCache<String> setCache = redisson.getSetCache("mySet");

        setCache.add("1", 20, TimeUnit.SECONDS);
        setCache.add("2");

        boolean done = setCache.contains("1");
        System.out.println("contains " + done);

        for (String elm : setCache) {
            System.out.println("elm " + elm);
        }

        done = setCache.remove("1");
        System.out.println("remove " + done);

        done = setCache.removeAll(Arrays.asList("1", "2", "3"));
        System.out.println("removeAll " + done);

        done = setCache.containsAll(Arrays.asList("4", "1", "0"));
        System.out.println("containsAll " + done);

        RSet<String> secondsSet = redisson.getSet("mySecondsSet");
        secondsSet.add("4");
        secondsSet.add("5");

        secondsSet.readAll();
        redisson.shutdown();
    }
}
