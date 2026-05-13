package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.Collection;

public class SetMultiMapExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        RSetMultimap<String, Integer> mySetMap = redisson.getSetMultimap("mySetMap");
        mySetMap.put("1", 1);
        mySetMap.put("1", 2);
        mySetMap.put("1", 3);
        mySetMap.put("2", 5);
        mySetMap.put("2", 6);
        mySetMap.put("4", 7);

        mySetMap.get("1");
        mySetMap.get("2");

        mySetMap.containsEntry("1", 3);
        mySetMap.entries();
        mySetMap.values();

        mySetMap.remove("1", 3);
        mySetMap.removeAll("1");

        Collection<Integer> newValues = Arrays.asList(5, 6, 7, 8, 9);
        mySetMap.putAll("5", newValues);

        mySetMap.replaceValues("2", newValues);
        mySetMap.getAll("2");

        redisson.shutdown();
    }
}
