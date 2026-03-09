package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;

import java.util.Arrays;

public class SortedSetExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RSortedSet<String> sortedSet = redisson.getSortedSet("mySortedSet");
        sortedSet.add("1");
        sortedSet.add("2");
        sortedSet.add("3");

        for (String elm : sortedSet) {
            System.out.println("elm " + elm);
        }

        sortedSet.first();
        sortedSet.last();

        sortedSet.remove("1");
        sortedSet.removeAll(Arrays.asList("1", "2", "3"));
        boolean done = sortedSet.containsAll(Arrays.asList("4", "1", "0"));
        System.out.println("containsAll " + done);

        redisson.shutdown();
    }
}
