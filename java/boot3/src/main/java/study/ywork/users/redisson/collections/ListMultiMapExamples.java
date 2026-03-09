package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RListMultimap;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.Collection;

public class ListMultiMapExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RListMultimap<String, Integer> myMap = redisson.getListMultimap("myMap");
        myMap.put("1", 1);
        myMap.put("1", 2);
        myMap.put("1", 3);
        myMap.put("2", 5);
        myMap.put("2", 6);
        myMap.put("4", 7);

        myMap.get("1");
        myMap.get("2");

        myMap.containsEntry("1", 3);
        myMap.entries();
        myMap.values();

        myMap.remove("1", 3);
        myMap.removeAll("1");

        Collection<Integer> newValues = Arrays.asList(5, 6, 7, 8, 9);
        myMap.putAll("5", newValues);

        myMap.replaceValues("2", newValues);
        myMap.getAll("2");

        myMap.fastRemove("2", "32");

        redisson.shutdown();
    }
}
