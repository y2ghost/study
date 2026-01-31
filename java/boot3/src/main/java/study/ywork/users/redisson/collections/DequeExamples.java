package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RDeque;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DequeExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RDeque<String> deque = redisson.getDeque("myQueue");
        deque.add("1");
        deque.add("2");
        deque.add("3");
        deque.add("4");

        boolean done = deque.contains("1");
        System.out.println("contains " + done);

        deque.peek();
        deque.poll();
        deque.element();

        for (String elm : deque) {
            System.out.println("elm " + elm);
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

        String firstValue = deque.pollFirst();
        System.out.println("firstValue " + firstValue);

        String lastValue = deque.pollLast();
        System.out.println("lastValue " + lastValue);

        String peekFirstValue = deque.peekFirst();
        System.out.println("peekFirstValue " + peekFirstValue);

        String peekLastValue = deque.peekLast();
        System.out.println("peekLastValue " + peekLastValue);

        String firstRemoved = deque.removeFirst();
        System.out.println("firstRemoved " + firstRemoved);

        String lastRemoved = deque.removeLast();
        System.out.println("lastRemoved " + lastRemoved);

        redisson.shutdown();
    }
}
