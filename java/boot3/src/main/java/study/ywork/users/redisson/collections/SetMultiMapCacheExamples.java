package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RSetMultimapCache;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class SetMultiMapCacheExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RSetMultimapCache<String, Integer> mySetCacheMap = redisson.getSetMultimapCache("mySetCacheMap");
        mySetCacheMap.put("1", 1);
        mySetCacheMap.put("1", 2);
        mySetCacheMap.put("1", 3);
        mySetCacheMap.put("2", 5);
        mySetCacheMap.put("2", 6);
        mySetCacheMap.put("4", 7);

        mySetCacheMap.expireKey("1", 10, TimeUnit.SECONDS);

        mySetCacheMap.get("1");
        mySetCacheMap.get("2");

        mySetCacheMap.containsEntry("1", 3);
        mySetCacheMap.entries();
        mySetCacheMap.values();

        mySetCacheMap.remove("1", 3);
        mySetCacheMap.removeAll("1");

        Collection<Integer> newValues = Arrays.asList(5, 6, 7, 8, 9);
        mySetCacheMap.putAll("5", newValues);

        mySetCacheMap.replaceValues("2", newValues);
        mySetCacheMap.getAll("2");

        mySetCacheMap.fastRemove("2", "32");
        redisson.shutdown();
    }
}
