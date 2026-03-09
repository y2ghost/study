package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScoredSortedSetExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RScoredSortedSet<String> set = redisson.getScoredSortedSet("mySortedSet");
        set.add(10, "1");
        set.add(20, "2");
        set.add(30, "3");

        for (String elm : set) {
            System.out.println("elm " + elm);
        }

        Map<String, Double> newValues = new HashMap<>();
        newValues.put("4", 40D);
        newValues.put("5", 50D);
        newValues.put("6", 60D);
        set.addAll(newValues);

        set.addScore("2", 10);
        set.contains("4");
        set.containsAll(Arrays.asList("3", "4", "5"));

        set.first();
        set.last();
        set.pollFirst();
        set.pollLast();
        set.readAll();

        redisson.shutdown();
    }
}
