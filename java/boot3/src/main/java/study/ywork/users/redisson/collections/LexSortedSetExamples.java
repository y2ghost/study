package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RLexSortedSet;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LexSortedSetExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RLexSortedSet sortedSet = redisson.getLexSortedSet("sortedSet");
        sortedSet.add("1");
        sortedSet.add("2");
        sortedSet.add("3");

        for (String elm : sortedSet) {
            System.out.println("elm " + elm);
        }

        Set<String> newValues = new HashSet<>();
        newValues.add("4");
        newValues.add("5");
        newValues.add("6");
        sortedSet.addAll(newValues);

        boolean done = sortedSet.contains("4");
        System.out.println("contains " + done);

        done = sortedSet.containsAll(Arrays.asList("3", "4", "5"));
        System.out.println("containsAll " + done);

        String firstValue = sortedSet.first();
        System.out.println("firstValue " + firstValue);

        String lastValue = sortedSet.last();
        System.out.println("lastValue " + lastValue);

        String polledFirst = sortedSet.pollFirst();
        System.out.println("polledFirst " + polledFirst);

        String polledLast = sortedSet.pollLast();
        System.out.println("polledLast " + polledLast);

        Collection<String> allValues = sortedSet.readAll();
        System.out.println("allValues " + allValues);

        redisson.shutdown();
    }
}
