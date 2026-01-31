package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        RList<String> myList = redisson.getList("myList");
        myList.add("1");
        myList.add("2");
        myList.add("3");

        boolean done = myList.contains("1");
        System.out.println("contains " + done);

        for (String elm : myList) {
            System.out.println("elm " + elm);
        }

        done = myList.remove("1");
        System.out.println("remove " + done);

        done = myList.removeAll(Arrays.asList("1", "2", "3"));
        System.out.println("removeAll " + done);

        done = myList.containsAll(Arrays.asList("4", "1", "0"));
        System.out.println("containsAll " + done);

        List<String> secondList = new ArrayList<>();
        secondList.add("4");
        secondList.add("5");
        myList.addAll(secondList);

        List<String> allValues = myList.readAll();
        System.out.println("allValues " + allValues);

        myList.addAfter("5", "7");
        myList.addBefore("4", "6");

        myList.fastSet(1, "6");
        myList.fastRemove(3);

        redisson.shutdown();
    }
}
