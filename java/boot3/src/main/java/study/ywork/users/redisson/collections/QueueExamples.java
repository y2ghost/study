package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueueExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        RQueue<String> queue = redisson.getQueue("myQueue");
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");

        boolean done = queue.contains("1");
        System.out.println("contains " + done);
        queue.peek();
        queue.poll();
        queue.element();

        for (String elm : queue) {
            System.out.println("elm " + elm);
        }

        done = queue.remove("1");
        System.out.println("remove " + done);

        done = queue.removeAll(Arrays.asList("1", "2", "3"));
        System.out.println("removeAll " + done);

        done = queue.containsAll(Arrays.asList("4", "1", "0"));
        System.out.println("containsAll " + done);

        List<String> secondList = new ArrayList<>();
        secondList.add("4");
        secondList.add("5");
        queue.addAll(secondList);

        RQueue<String> secondQueue = redisson.getQueue("mySecondQueue");
        queue.pollLastAndOfferFirstTo(secondQueue.getName());
        redisson.shutdown();
    }
}
