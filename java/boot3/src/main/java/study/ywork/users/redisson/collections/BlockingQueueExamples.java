package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BlockingQueueExamples {
    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RBlockingQueue<String> queue = redisson.getBlockingQueue("myQueue");
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");

        boolean done = queue.contains("1");
        System.out.println("contains " + done);

        System.out.println("peek " + queue.peek());
        System.out.println("poll " + queue.poll());
        System.out.println("poll " + queue.element());

        for (String elm : queue) {
            System.out.println(elm);
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

        Thread t = new Thread(() -> {
            try {
                String element = queue.poll(10, TimeUnit.SECONDS);
                System.out.println("element " + element);

                element = queue.take();
                System.out.println("element " + element);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        t.start();
        t.join();
        redisson.shutdown();
    }
}
