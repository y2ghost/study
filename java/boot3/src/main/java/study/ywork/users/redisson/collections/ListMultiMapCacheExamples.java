package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RListMultimapCache;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class ListMultiMapCacheExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RListMultimapCache<String, Integer> myMultiMap = redisson.getListMultimapCache("myMultiMap");
        myMultiMap.put("1", 1);
        myMultiMap.put("1", 2);
        myMultiMap.put("1", 3);
        myMultiMap.put("2", 5);
        myMultiMap.put("2", 6);
        myMultiMap.put("4", 7);
        myMultiMap.expireKey("1", 10, TimeUnit.SECONDS);

        myMultiMap.get("1");
        myMultiMap.get("2");

        myMultiMap.containsEntry("1", 3);
        myMultiMap.entries();
        myMultiMap.values();

        myMultiMap.remove("1", 3);
        myMultiMap.removeAll("1");

        Collection<Integer> newValues = Arrays.asList(5, 6, 7, 8, 9);
        myMultiMap.putAll("5", newValues);

        myMultiMap.replaceValues("2", newValues);
        myMultiMap.getAll("2");

        myMultiMap.fastRemove("2", "32");

        redisson.shutdown();
    }
}
