package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Arrays;

public class SetExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RSet<String> set = redisson.getSet("mySet");
        set.add("1");
        set.add("2");
        set.add("3");

        boolean done = set.contains("1");
        System.out.println("contains " + done);

        for (String elm : set) {
            System.out.println("elm " + elm);
        }

        done = set.remove("1");
        System.out.println("remove " + done);

        done = set.removeAll(Arrays.asList("1", "2", "3"));
        System.out.println("removeAll " + done);

        done = set.containsAll(Arrays.asList("4", "1", "0"));
        System.out.println("containsAll " + done);

        set.removeRandom();
        set.random();

        RSet<String> secondsSet = redisson.getSet("mySecondsSet");
        secondsSet.add("4");
        secondsSet.add("5");


        set.union(secondsSet.getName());
        set.readUnion(secondsSet.getName());


        set.diff(secondsSet.getName());
        set.readDiff(secondsSet.getName());


        set.intersection(secondsSet.getName());
        set.readIntersection(secondsSet.getName());

        set.readAll();

        redisson.shutdown();
    }
}
