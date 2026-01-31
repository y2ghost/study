package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BlockingDequeExamples {
    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        RBlockingDeque<String> deque = redisson.getBlockingDeque("myQueue");
        deque.add("1");
        deque.add("2");
        deque.add("3");
        deque.add("4");

        boolean done = deque.contains("1");
        System.out.println("contains " + done);

        System.out.println("peek " + deque.peek());
        deque.poll();
        deque.element();

        for (String elm : deque) {
            System.out.println(elm);
        }

        done = deque.remove("1");
        System.out.println("remove " + done);

        done = deque.removeAll(Arrays.asList("1", "2", "3"));
        System.out.println("removeAll " + done);

        done = deque.containsAll(Arrays.asList("4", "1", "0"));
        System.out.println("containsAll " + done);

        List<String> secondList = new ArrayList<>();
        secondList.add("4");
        secondList.add("5");
        deque.addAll(secondList);

        RQueue<String> secondQueue = redisson.getQueue("mySecondQueue");

        deque.pollLastAndOfferFirstTo(secondQueue.getName());

        deque.addLast("8");
        deque.addFirst("9");

        deque.addLast("30");
        deque.addFirst("80");

        System.out.println("pollFirst " + deque.pollFirst());
        System.out.println("pollLast " + deque.pollLast());

        System.out.println("peekFirst " + deque.peekFirst());
        System.out.println("peekLast " + deque.peekLast());

        System.out.println("removeFirst " + deque.removeFirst());
        System.out.println("removeLast " + deque.removeLast());

        Thread t = new Thread(() -> {
            try {
                System.out.println("thread start");
                String element = deque.poll(10, TimeUnit.SECONDS);
                System.out.println("poll: " + element);

                element = deque.take();
                System.out.println("take: " + element);

                element = deque.pollFirst(3, TimeUnit.SECONDS);
                System.out.println("pollFirst: " + element);

                element = deque.pollLast(3, TimeUnit.SECONDS);
                System.out.println("pollLast: " + element);

                element = deque.takeFirst();
                System.out.println("takeFirst: " + element);

                element = deque.takeLast();
                System.out.println("takeLast: " + element);

                element = deque.pollLastAndOfferFirstTo(secondQueue.getName(), 4, TimeUnit.SECONDS);
                System.out.println("pollLastAndOfferFirstTo: " + element);
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
